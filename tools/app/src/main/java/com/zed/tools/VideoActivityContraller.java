package com.zed.tools;

public interface VideoActivityContraller {
    void showAllContainer();

    void showVideoFullContainer(int index);

    int isFullContainer();

    void toggleFullScreen(int index);

    boolean isFullContainer(int index);
}
