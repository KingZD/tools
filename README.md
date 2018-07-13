# tools
项目用到的自定义组件集合

持续更新ing


|主界面|卡尺界面|四界面播放|双击其中一个界面铺满|
|--|--|--|--|
|![](https://github.com/KingZD/Resource/blob/master/tools/device-2018-07-10-133316.png?raw=true)|![](https://github.com/KingZD/Resource/blob/master/tools/device-2018-07-13-092205.png?raw=true)|![](https://github.com/KingZD/Resource/blob/master/tools/device-2018-07-10-133405.png?raw=true)|![](https://github.com/KingZD/Resource/blob/master/tools/device-2018-07-10-133417.png?raw=true)|


以上描述了两个功能，卡尺包括无限循环和不循环两种方式，改编自[LoopScaleView](https://github.com/PandaQAQ/LoopScale/blob/master/loopscaleview/src/main/java/com/pandaq/loopscaleview/LoopScaleView.java),修复了一些小问题并加上了不循环的功能 ，一些定义的属性可以在attrs里面查看暂时不写出来了

视频多格播放使用了两个开源的播放器 具体支持什么格式视频请点进去看，基本的都有**rtmp mp4 u3u8**等常见视频格式

|[GSYVideoPlayer](https://github.com/CarGuo/GSYVideoPlayer)|[jjdxm_ijkplayer](https://github.com/jjdxmashl/jjdxm_ijkplayer)|
|--|--|


新增卡尺多时间段展示模式 Rule为多时间段 Rule_1为循环不循环普通模式

