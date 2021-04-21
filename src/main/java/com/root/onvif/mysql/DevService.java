package com.root.onvif.mysql;

import com.root.onvif.mbg.model.TbDev;

import java.util.List;

public interface DevService {
    List<TbDev> listAll();
    int create(TbDev info);
    int delete(String ip);
}
