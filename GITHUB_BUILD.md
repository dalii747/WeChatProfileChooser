# 在线编译说明

## 步骤1: 上传到GitHub

1. 在GitHub创建新仓库(可以是私有仓库)
2. 解压项目: `tar -xzf WeChatProfileChooser.tar.gz`
3. 上传到GitHub:
```bash
cd WeChatProfileChooser
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/你的用户名/仓库名.git
git push -u origin main
```

## 步骤2: 触发编译

1. 进入GitHub仓库页面
2. 点击 "Actions" 标签
3. 点击 "Build APK" 工作流
4. 点击右侧 "Run workflow" 按钮
5. 点击绿色的 "Run workflow" 确认

## 步骤3: 下载APK

1. 等待编译完成(约3-5分钟)
2. 编译完成后,点击工作流运行记录
3. 在 "Artifacts" 部分找到 "wechat-profile-chooser"
4. 点击下载APK

## 步骤4: 安装

1. 将APK传输到手机
2. 安装APK
3. 打开LSPosed Manager
4. 勾选"微信多开选择器"模块
5. 选择作用域为"微信"
6. 重启微信

## 测试

1. 打开企业微信
2. 点击"微信登录"
3. 应该会弹出选择器
4. 选择要使用的微信

完成!
