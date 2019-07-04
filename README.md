# 视频播放器
## 简介
1. 该播放器是基于ijkplayer+第三方视频播放器gsyVideoPlayer做的二次封装
2. 主要实现了修改底层ffmpeg源码,实现自定义的视频解密过程
3. 同时播放器增加了离线视频播放以及视频拖动缩略图功能
4. 其他功能请参照https://github.com/CarGuo/GSYVideoPlayer,查阅相关功能

## Demo使用
1. libs目录下有一个aar包文件,player-1.0.aar,该文件包含了所有cpu架构的so文件以及视频播放器源码,aar包引用方式请参考https://blog.csdn.net/luoyingxing/article/details/78353305 注意aar文件不能引入远程依赖,比如okhttp,ijkplayer等源码,因此在demo中在外部进行了引用
`implementation viewDependencies.exo_player2`
`implementation viewDependencies.exo_player2_rtmp`
`implementation viewDependencies.ijkplayer_java`
`implementation viewDependencies.ijkplayer_exo`
`implementation viewDependencies.transitionseverywhere`
`implementation viewDependencies.ijkplayer_java`

2. 启动应用,demo首页有4个按钮,分别是http离线播放,http在线播放,https离线播放,https在线播放,离线播放的数据暂时放在本地,如果需要通过接口获取,请自行实现相应功能
3. 犹豫hls不支持本地文件播放,因此需要创建本地的http和https server,demo中使用了androidasync进行本地server的创建,地址https://github.com/koush/AndroidAsync
4. seekBar滑动缩略图为本地图片,需自行修改,通过接口获取



