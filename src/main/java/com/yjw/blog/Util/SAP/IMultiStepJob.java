package com.yjw.blog.Util.SAP;

public interface IMultiStepJob {
    public boolean runNextStep();

    String getName();

    public void cleanUp();

}
