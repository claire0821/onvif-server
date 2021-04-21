package com.root.onvif.mysql.impl;

import com.root.onvif.mbg.mapper.TbDevMapper;
import com.root.onvif.mbg.model.TbDev;
import com.root.onvif.mbg.model.TbDevExample;
import com.root.onvif.mysql.DevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevServiceImpl implements DevService {
    @Autowired
    TbDevMapper tbDevMapper;

    @Override
    public List<TbDev> listAll() {
        return tbDevMapper.selectByExample(new TbDevExample());
    }

    @Override
    public int create(TbDev info) {
        return tbDevMapper.insert(info);
    }

    @Override
    public int delete(String ip) {
        TbDevExample tbDevExample = new TbDevExample();
        tbDevExample.createCriteria().andIpEqualTo(ip);
        return tbDevMapper.deleteByExample(tbDevExample);
    }
}
