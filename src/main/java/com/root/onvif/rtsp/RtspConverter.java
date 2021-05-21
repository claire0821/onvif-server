package com.root.onvif.rtsp;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVCodecParameters;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avformat.AVOutputFormat;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avformat.Read_packet_Pointer_BytePointer_int;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.bytedeco.ffmpeg.global.avcodec.*;
import static org.bytedeco.ffmpeg.global.avcodec.av_packet_unref;
import static org.bytedeco.ffmpeg.global.avformat.*;
import static org.bytedeco.ffmpeg.global.avformat.av_dump_format;
import static org.bytedeco.ffmpeg.global.avutil.*;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_alloc;

public class RtspConverter extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(RtspConverter.class);

    //流地址
    public String url;

    //读流器
    private FFmpegFrameGrabber grabber;
    //转码器
    private FFmpegFrameRecorder recorder;

    //转FLV格式的头信息<br/>
    //如果有第二个客户端播放首先要返回头信息
    private byte[] headers;

    //保存转换好的流
    private ByteArrayOutputStream stream;

    private byte[] outData;
    private long updateTime;

    private RtspState rtspState;

    private boolean close;

    public RtspState getRtspState() {
        return rtspState;
    }

    public void setRtspState(RtspState rtspState) {
        this.rtspState = rtspState;
    }

    public RtspConverter(String url) {
        this.url = url;
        this.updateTime = System.currentTimeMillis();
//        avformat_network_init();
        close = false;
        avutil.av_log_set_level(AV_LOG_WARNING);
        FFmpegLogCallback.set();
        this.rtspState = RtspState.INITIAL;
    }

    public byte[] getHeader() {
        if(headers == null || headers.length <= 0) {
            return null;
        }
        return headers;
    }

    public byte[] getOutData() {
        return outData;
    }

    public void setOutData(byte[] outData) {
        this.outData = outData;
    }

//    public void getOut(HttpServletResponse response) throws IOException, InterruptedException {
//        if(out != null && out.length > 0) {
//            response.getOutputStream().write(out);
//        }
//        Thread.sleep(100);
//        getOut(response);
//    }


    private boolean createGrabber() {
        boolean res = false;
        try {
            grabber = new FFmpegFrameGrabber(url);

            if("rtsp".equals(url.substring(0,4))) {
                //设置打开协议tcp / udp udp_multicast使用UDP多播作为较低的传输协议 http使用HTTP隧道作为较低的传输协议
                grabber.setOption("rtsp_transport", "tcp");
                //首选TCP进行RTP传输
                //filter_src：只接受来自协商的对等地址和端口的数据包。
                //listen：充当服务器，监听传入的连接。
                //prefer_tcp：如果TCP可用作RTSP RTP传输，请先尝试使用TCP进行RTP传输。
                grabber.setOption("rtsp_flags", "prefer_tcp");
                //初始暂停 如果设置为1，请不要立即开始播放流。默认值为0。
                //grabber.setOption("initial_pause", "0");
                //允许的媒体类型 vide audio data
//                grabber.setOption("allowed_media_types", "video");
                grabber.setOption("stimeout", "15000000");
            }
            // 设置缓存大小，提高画质、减少卡顿花屏
            grabber.setOption("buffer_size", "1024000");
            //设置视频比例
            //grabber.setAspectRatio(1.7777);
            grabber.setPixelFormat(AV_PIX_FMT_YUV420P);
            grabber.setOption("threads", "1");

            grabber.start();
            grabber.flush();
            res = true;
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
            res = false;
        } finally {
            return res;
        }
    }

    //转换成flv
    public boolean createRecorder() {
        boolean res = false;
        try{
            stream = new ByteArrayOutputStream();
            //创建转码器
            recorder = new FFmpegFrameRecorder(stream, grabber.getImageWidth(), grabber.getImageHeight(),
                    grabber.getAudioChannels());
            if(true) {
                recorder.setInterleaved(false);
                recorder.setVideoOption("tune", "zerolatency");
                recorder.setVideoOption("preset", "ultrafast");
                recorder.setVideoOption("crf", "26");
                recorder.setVideoOption("threads", "1");
                //提供输出流封装格式
                recorder.setFormat("flv");
                // 视频帧率(保证视频质量的情况下最低25，低于25会出现闪屏
                recorder.setFrameRate(50);// 设置帧率
                // 关键帧间隔，一般与帧率相同或者是视频帧率的两倍
                recorder.setGopSize(50 * 2);// 设置gop
//		recorder.setVideoBitrate(500 * 1000);// 码率500kb/s
                recorder.setVideoCodecName("libx264");
//		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
//		recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
                recorder.setAudioCodecName("aac");
                recorder.setAudioChannels(grabber.getAudioChannels());
            } else {
                recorder.setFormat("flv");
                //配置转码器
                recorder.setFrameRate(grabber.getFrameRate());
                recorder.setSampleRate(grabber.getSampleRate());
                if (grabber.getAudioChannels() > 0) {
                    recorder.setAudioChannels(grabber.getAudioChannels());
                    recorder.setAudioBitrate(grabber.getAudioBitrate());
                    recorder.setAudioCodec(grabber.getAudioCodec());
                    //设置视频比例
                    //recorder.setAspectRatio(grabber.getAspectRatio());
                }

                recorder.setVideoBitrate(grabber.getVideoBitrate());//码率
                recorder.setVideoCodec(grabber.getVideoCodec());
            }

            recorder.start(grabber.getFormatContext());
//            grabber.flush();
            //设置头信息
            this.headers = stream.toByteArray();
            stream.reset();
            res = true;
        } catch (FrameRecorder.Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            res = false;
        } finally {
            return res;
        }
    }

    public void toFlv() {
        if(!createGrabber()) {
            this.rtspState = RtspState.CLOSE;
            logger.error("open stream failed " + this.url);
            return;
        }
        logger.info("open stream success " + this.url);
        if(!createRecorder()) {
            this.rtspState = RtspState.CLOSE;
            logger.error("open record failed " + this.url);
            return;
        }
        logger.info("open record success " + this.url);
        this.rtspState = RtspState.OPEN;


        while (true) {
            //FFmpeg读流压缩
            AVPacket k = null;
            Frame frame = null;
            try {
                if(true) {
                    k = grabber.grabPacket();
                    if(k!= null) {
                        recorder.recordPacket(k);//转换器转换
                    }
                } else {
                    frame = grabber.grabFrame();
                    if(frame != null) {
                        recorder.record(frame);
                    }
                }

                if(stream.size() > 0) {
                    setOutData(stream.toByteArray());
                    stream.reset();
                    setRtspState(RtspState.RUN);
                }
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
                continue;
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
                continue;
            }
        }

//        try {
//            grabber.close();
//            recorder.close();
//            stream.close();
//        } catch (FrameGrabber.Exception e) {
//            e.printStackTrace();
//        } catch (FrameRecorder.Exception e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            this.rtspState = RtspState.CLOSE;
//        }
    }
    @Override
    public void run() {
        toFlv();
    }
    public void run4() {
        try {
            grabber = new FFmpegFrameGrabber(url);
//            grabber.setFormat("rtsp");
            //设置打开协议tcp / udp
            grabber.setOption("rtsp_transport", "tcp");
            //设置未响应超时时间 0.5秒
            grabber.setOption("stimeout", "500000");
            //设置缓存大小，提高画质、减少卡顿花屏
            //grabber.setOption("buffer_size", "1024000");
            //设置视频比例
            //grabber.setAspectRatio(1.7777);

            grabber.start();

            stream = new ByteArrayOutputStream();
            //创建转码器
            recorder = new FFmpegFrameRecorder(stream, grabber.getImageWidth(), grabber.getImageHeight(),
                    grabber.getAudioChannels());
            //配置转码器
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setSampleRate(grabber.getSampleRate());
            if (grabber.getAudioChannels() > 0) {
                recorder.setAudioChannels(grabber.getAudioChannels());
                recorder.setAudioBitrate(grabber.getAudioBitrate());
                recorder.setAudioCodec(grabber.getAudioCodec());
                //设置视频比例
                //recorder.setAspectRatio(grabber.getAspectRatio());
            }
            recorder.setFormat("flv");

            recorder.setVideoBitrate(grabber.getVideoBitrate());//码率
            recorder.setVideoCodec(grabber.getVideoCodec());
            recorder.start(grabber.getFormatContext());

            if (headers == null) {
                headers = stream.toByteArray();
                stream.reset();
            }
            setRtspState(RtspState.OPEN);

            while (true) {
                //FFmpeg读流压缩
                AVPacket k = grabber.grabPacket();
                if (k != null) {
                    try {
                        //转换器转换
                        recorder.recordPacket(k);
                    } catch (Exception e) {
                    }

                    setOutData(stream.toByteArray());
                    System.out.println("record");
                    stream.reset();
                    setRtspState(RtspState.RUN);

                }
            }
        } catch(FrameGrabber.Exception e) {
            System.out.println("grabber error:");
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            System.out.println("recorder error:");
            e.printStackTrace();
        }


    }

//    @Override
    public void run1() {
        int videoindex = -1;
        int audioindex = -1;
        AVCodecContext video_c;//一个描述编解码器上下文的数据结构
        AVFormatContext ifmt_ctx = new AVFormatContext(null);//输入流信息
        AVFormatContext ofmt_ctx = new AVFormatContext(null);//输出流信息
        AVOutputFormat ofmt = new AVOutputFormat(null);//输出格式

        int ret = -1;
        AVDictionary inOptions = new AVDictionary();

        //设置打开协议
        av_dict_set(inOptions, "rtsp_transport", "tcp", 0);
        av_dict_set(inOptions, "max_delay", "500000", 0);

        //打开输入流，解封文件头
        ret = avformat_open_input(ifmt_ctx, url, null, inOptions);
        if (ret < 0) {
            throw new RuntimeException("Could not open input file");
        }

        //读取一部分视音频数据并且获得一些相关的信息
        ret = avformat_find_stream_info(ifmt_ctx, (PointerPointer) null);
        if (ret < 0) {
            throw new RuntimeException("Failed to retrieve input stream information");
        }

        //查找视频编码索引
        AVStream video_st = null;
        AVStream audio_st = null;
        AVCodecParameters video_par = null, audio_par = null;
        int nb_streams = ifmt_ctx.nb_streams();//nb_streams代表有几路流，一般是2路：即音频和视频，顺序不一定
        for (int i = 0; i < nb_streams; i++) {
            AVStream st = ifmt_ctx.streams(i);
            // Get a pointer to the codec context for the video or audio stream
            AVCodecParameters par = st.codecpar();
            if (video_st == null && par.codec_type() == AVMEDIA_TYPE_VIDEO) {
                video_st = st;
                video_par = par;
                videoindex = i;//这一路是视频流，标记一下，以后取视频流都从ifmt_ctx->streams[video_index]取
            } else if (audio_st == null && par.codec_type() == AVMEDIA_TYPE_AUDIO) {
                audio_st = st;
                audio_par = par;
                audioindex = i;
            }
        }

        //打印关于输入或输出格式的详细信息
        int is_output = 0;//输入(0)输出(1)
        av_dump_format(ifmt_ctx, 0, url, 0);

        //打开输出流
        String out_filename = "";
        String format_name = "flv";
        if (avformat_alloc_output_context2(ofmt_ctx, (AVOutputFormat) null, format_name, out_filename) < 0) {
            System.out.println("out format error");
            return;
        }
        if (ofmt_ctx == null) {
            return;//
        }

        //输出格式
//        ofmt = avformat.av_guess_format(format_name, out_filename, (String)null);
        ofmt = ofmt_ctx.oformat();

        is_output = 1;
        av_dump_format(ofmt_ctx, 0, out_filename, is_output);

//        this.avio = avformat.avio_alloc_context(new BytePointer(avutil.av_malloc(4096L)), 4096, 1, this.oc, (Read_packet_Pointer_BytePointer_int)null, writeCallback, this.outputStream instanceof Seekable ? seekCallback : null);
//        this.oc.pb(this.avio);
//        this.filename = this.outputStream.toString();

        //写文件头到输出文件
        ret = avformat_write_header(ofmt_ctx, new AVDictionary(null));
        if (ret < 0) {
            System.out.println("Error occured when opening output URL");
            return;
        }

        AVPacket packet = av_packet_alloc();
        while (true) {
            AVStream in_stream = new AVStream(null);
            AVStream out_stream = new AVStream(null);

            //从输入流获取一个数据包
            ret = av_read_frame(ifmt_ctx,packet);
            if(ret < 0) {
                av_packet_unref(packet);
                continue;
            }

            in_stream = ifmt_ctx.streams(packet.stream_index());
            out_stream = ofmt_ctx.streams(packet.stream_index());
            //copy packet
            //转换 PTS/DTS 时序
            packet.stream_index(videoindex);
            packet.pts(av_rescale_q_rnd(packet.pts(),in_stream.time_base(),out_stream.time_base(),AV_ROUND_NEAR_INF|AV_ROUND_PASS_MINMAX));
            packet.dts(avutil.av_rescale_q_rnd(packet.dts(), in_stream.time_base(), out_stream.time_base(), AV_ROUND_NEAR_INF|AV_ROUND_PASS_MINMAX));
            packet.duration(av_rescale_q(packet.duration(),in_stream.time_base(),out_stream.time_base()));
            packet.pos(-1L);

//            if(packet.stream_index() == 0) {
//
//            }
            ret = av_write_frame(ofmt_ctx, packet);
            if (ret >= 0)
            {
                AVStream streams = ofmt_ctx.streams(videoindex);
            }
//            ret = av_interleaved_write_frame(ofmt_ctx,packet);

            av_packet_unref(packet);
        }
    }

    public void run2() {
        int videoindex = -1;
        int audioindex = -1;
        AVCodecContext video_c = null;//一个描述编解码器上下文的数据结构
        AVFormatContext ifmt_ctx = new AVFormatContext(null);//输入流信息
        AVFormatContext ofmt_ctx = new AVFormatContext(null);//输出流信息
        AVOutputFormat ofmt = new AVOutputFormat(null);//输出格式

        int ret = -1;
        AVDictionary inOptions = new AVDictionary();

        //设置打开协议
        av_dict_set(inOptions, "rtsp_transport", "tcp", 0);
        av_dict_set(inOptions, "max_delay", "500000", 0);

        //打开输入流，解封文件头
        ret = avformat_open_input(ifmt_ctx, url, null, inOptions);
        if (ret < 0) {
            throw new RuntimeException("Could not open input file");
        }

        //读取一部分视音频数据并且获得一些相关的信息
        ret = avformat_find_stream_info(ifmt_ctx, (PointerPointer) null);
        if (ret < 0) {
            throw new RuntimeException("Failed to retrieve input stream information");
        }

        //查找视频编码索引
        AVStream video_st = null;
        AVStream audio_st = null;
        AVCodecParameters video_par = null, audio_par = null;
        int nb_streams = ifmt_ctx.nb_streams();//nb_streams代表有几路流，一般是2路：即音频和视频，顺序不一定
        for (int i = 0; i < nb_streams; i++) {
            AVStream st = ifmt_ctx.streams(i);
            // Get a pointer to the codec context for the video or audio stream
            AVCodecParameters par = st.codecpar();
            if (video_st == null && par.codec_type() == AVMEDIA_TYPE_VIDEO) {
                video_st = st;
                video_par = par;
                videoindex = i;//这一路是视频流，标记一下，以后取视频流都从ifmt_ctx->streams[video_index]取
            } else if (audio_st == null && par.codec_type() == AVMEDIA_TYPE_AUDIO) {
                audio_st = st;
                audio_par = par;
                audioindex = i;
            }
        }
        if(videoindex != -1 && video_par != null) {
            //编解码上下文
            //查找解码器 编码格式
            AVCodec codec = avcodec_find_decoder(video_par.codec_id());

            if (codec == null) {
                System.out.println("Codec not found");
            }
            // 申请AVCodecContext空间
            if ((video_c = avcodec_alloc_context3(codec)) == null) {
                System.out.println("avcodec_alloc_context3() error: Could not allocate video decoding context.");
            }
            //该函数用于将流里面的参数，也就是AVStream里面的参数直接复制到AVCodecContext的上下文当中
            if ((ret = avcodec_parameters_to_context(video_c, video_st.codecpar())) < 0) {
                System.out.println("avcodec_parameters_to_context() error: Could not copy the video stream parameters.");
            }

        }
        //打印关于输入或输出格式的详细信息
        int is_output = 0;//输入(0)输出(1)
        av_dump_format(ifmt_ctx, 0, url, 0);

        //打开输出流
        String out_filename = "";
        String format_name = "flv";
        if (avformat_alloc_output_context2(ofmt_ctx, (AVOutputFormat) null, format_name, out_filename) < 0) {
            System.out.println("out format error");
            return;
        }
        if (ofmt_ctx == null) {
            return;//
        }

        //输出格式
//        ofmt = avformat.av_guess_format(format_name, out_filename, (String)null);
        ofmt = ofmt_ctx.oformat();

        is_output = 1;
        av_dump_format(ofmt_ctx, 0, out_filename, is_output);

//        this.avio = avformat.avio_alloc_context(new BytePointer(avutil.av_malloc(4096L)), 4096, 1, this.oc, (Read_packet_Pointer_BytePointer_int)null, writeCallback, this.outputStream instanceof Seekable ? seekCallback : null);
//        this.oc.pb(this.avio);
//        this.filename = this.outputStream.toString();

        //写文件头到输出文件
        ret = avformat_write_header(ofmt_ctx, new AVDictionary(null));
        if (ret < 0) {
            System.out.println("Error occured when opening output URL");
            return;
        }

        AVPacket packet = av_packet_alloc();
        //创建转码器
        recorder = new FFmpegFrameRecorder(stream, video_c.width(), video_c.height(), video_c.channels());
        //配置转码器
//        recorder.setFrameRate(video_c.framerate().);
        recorder.setSampleRate(grabber.getSampleRate());

        recorder.setFormat("flv");

        recorder.setVideoBitrate((int)video_c.bit_rate());//码率
        recorder.setVideoCodec(video_c.codec_id());
//        recorder.start(ifmt_ctx);

        if (headers == null) {
            headers = stream.toByteArray();
            stream.reset();
        }
        setRtspState(RtspState.OPEN);

//        while (true) {
//            //FFmpeg读流压缩
//            AVPacket k = grabber.grabPacket();
//            if (k != null) {
//                try {
//                    //转换器转换
//                    recorder.recordPacket(k);
//                } catch (Exception e) {
//                }
//
//                setOutData(stream.toByteArray());
//                System.out.println("record");
//                stream.reset();
//                setRtspState(RtspState.RUN);
//
//            }
//        }
//            ret = av_interleaved_write_frame(ofmt_ctx,packet);

            av_packet_unref(packet);
    }
}
