// Server 基本信息 
config.debug = 1;//生产模式:0 调式模式:1
config.charset = "UTF-8";//系统的字符编码
config.version = "1.0.0.1";//系统配置的版本号
config.resourceVersion = "1.0.0.0";//系统配置的资源版本号
config.dbVersion = "0.2.0.0";//系统配置的数据库版本号
config.regionId = "1";//区id
config.serverId = "100001";// 服务器ID

// 如果需要让其他人连接自己的机器, 可以修改这个 IP 地址
config.bindIp = "192.168.1.41"; //服务绑定的IP

//服务器索引;
config.serverIndex=2;
config.ports = "9000";
config.serverName = "texas";

config.serverHost = "192.168.1.41";//服务器ID，没用

config.serverDomain = "texas";//服务器组的域名 s1.l.mop.com
config.ioProcessor = 4;//每个端口IO处理的线程个数
config.language = "zh_CN";
config.i18nDir = "i18n";

// Excel 资源目录
config.baseResourceDir = "../resources";
config.scriptDir = "scripts";
config.flashSocketPolicy = "<cross-domain-policy>\r\n<allow-access-from domain=\"*\" to-ports=\"80-65535\" />\r\n </cross-domain-policy>\r\n\0";

config.dbInitType = 0;
config.dbConfigName = "game_server_hibernate.cfg.xml,game_server_hibernate_query.xml";
config.lastNetOnOff = true;//是否开启超时踢出玩家

config.maxOnlineUsers = 1500;
config.loginWallEnabled = false;

/*
 * 配置Telnet
 */
config.telnetServerName = "GameServer_telnet";
config.telnetBindIp = "192.168.1.8";
config.telnetPort = 7000;

// 模板
config.templateXorLoad = false;
config.timeZone="Asia/Shanghai"; //初始化时区

// redis
config.redisHost = "127.0.0.1";//链接redis的IP 192.168.1.100  127.0.0.1
config.redisPort = 6379;
config.redisMaxActive = 1500;
config.redisMaxIdle = 10;
config.redisMaxWait = 5;
config.redisMaxWait = 5;
config.password = "netherfire123!";

// log
config.logHost = "127.0.0.1";//log服务器IP地址
config.logPort = 7001;
config.logEnable = true;

config.test = true;//测试   上线改成false

config.robot =true;//false:没有开启机器人 true:开启机器人
config.robotServerIp = "127.0.0.1";//机器人IP
config.robotServerPort =6666;

config.smsaddress = "http://222.73.117.169/msg/";
config.smsPhoneNumber = "13691096041,13521815300,13810395415,13811745499";
config.smsAccount= "N6659582";
config.smsPwd = "Psba140b";
config.smsMaxpeople = 1000;
config.smsContent = "当前服务器人数已达到N人，请及时关注服务器状态。";

config.httpDataAddress = "127.0.0.1";//47.88.148.184   127.0.0.1  47.88.138.241
config.httpDataPort = 8888;//外部获取数据接口


config.boquSendMsgUrl = "http://www.bo-qu.com/console/api/sms";//发送验证码的接口
config.clientId=8;//博趣
config.clientSecret="1Pkbu1X9mxjTtBw9YscmniqoUV92TAZxpslxD45V";//博趣





