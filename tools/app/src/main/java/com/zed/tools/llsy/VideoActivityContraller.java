package com.zed.tools.llsy;

public interface VideoActivityContraller {
    void showAllContainer();

    void showVideoFullContainer(int index);

    int isFullContainer();

    void toggleFullScreen(int index);
}
