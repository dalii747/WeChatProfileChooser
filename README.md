# 微信多开选择器 - LSPosed模块

## 功能说明
拦截企业微信的OAuth登录请求,允许用户选择使用主空间微信或Island工作区微信进行授权。

## 技术原理

### 微信OAuth授权流程
```
企业微信 (发起OAuth请求)
  ↓ explicit Intent with OAuth params
WXEntryActivity.onCreate
  ↓ 转发Intent到UIEntryStub
UIEntryStub.onResume
  ↓ 创建异步任务
m.handleWXAppMessage (COMMAND_SENDAUTH)
  ↓ 调用插件加载器
Lex4/l.j("webview", ".ui.tools.SDKOAuthUI", intent)
  ↓
SDKOAuthUI (显示授权页面)
```

### Hook实现
- **Hook点**: `com.tencent.mm.plugin.base.stub.WXEntryActivity.onCreate`
- **时机**: 在onCreate方法执行前拦截
- **操作**:
  1. 显示AlertDialog让用户选择微信
  2. 如果选择主空间:调用原onCreate方法
  3. 如果选择Island:使用Intent.toUri()序列化Intent,通过`am start --user 12`转发到Island微信

## 编译方法

### 方法1: 使用Android Studio (推荐)
1. 解压项目: `tar -xzf WeChatProfileChooser.tar.gz`
2. 用Android Studio打开项目
3. 等待Gradle同步完成
4. Build → Build Bundle(s) / APK(s) → Build APK(s)
5. 生成的APK在 `app/build/outputs/apk/release/`

### 方法2: 使用命令行
```bash
cd WeChatProfileChooser
./gradlew assembleRelease
```

### 方法3: 在线编译
可以使用以下在线服务:
- GitHub Actions (推荐)
- AppVeyor
- Travis CI

## 安装和使用

1. 安装编译好的APK到手机
2. 打开LSPosed Manager
3. 在"模块"中找到"微信多开选择器"并勾选
4. 在"作用域"中选择"微信"(com.tencent.mm)
5. 重启微信
6. 在企业微信中点击"微信登录"
7. 会弹出选择器,选择要使用的微信

## 注意事项

1. 需要LSPosed框架(已验证你安装了zygisk_lsposed)
2. 需要root权限
3. Island工作区的用户ID默认为12,如果不同需要修改代码
4. 微信可能有反Hook检测,如果不工作可能需要使用隐藏模块功能

## 文件说明

- `app/src/main/java/com/wechat/profilechooser/MainHook.java` - Hook实现
- `app/src/main/AndroidManifest.xml` - 模块声明
- `app/src/main/assets/xposed_init` - Xposed入口声明
- `app/build.gradle` - 构建配置

## 故障排除

### 模块不生效
1. 确认LSPosed中已勾选模块
2. 确认作用域选择了微信
3. 完全重启微信(杀掉进程)
4. 查看LSPosed日志

### 选择器不显示
1. 检查LSPosed日志是否有错误
2. 确认Hook点是否正确
3. 可能被微信的反Hook检测拦截

### Intent转发失败
1. 确认Island微信的用户ID(默认12)
2. 检查su权限是否正常
3. 查看logcat日志

## 开发者信息

基于对微信8.x版本的逆向分析开发。
Hook点: WXEntryActivity.onCreate (com.tencent.mm.plugin.base.stub.WXEntryActivity)
