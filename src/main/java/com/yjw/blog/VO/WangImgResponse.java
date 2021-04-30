package com.yjw.blog.VO;

import java.util.List;

public class WangImgResponse {
    int errno;
    List<WangEditImg> data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public List<WangEditImg> getData() {
        return data;
    }

    public void setData(List<WangEditImg> data) {
        this.data = data;
    }
}
