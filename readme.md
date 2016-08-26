# 开发须知
1. 数据库
    - 新建数据库portal
    - 新建用户portal,密码portal123
    - 将数据库portal的所有权限赋予用户portal

            create database if not exists portal character set utf8;
            CREATE USER 'portal'@'%' IDENTIFIED BY 'portal123';
            CREATE USER 'portal'@'localhost' IDENTIFIED BY 'portal123';
            grant all privileges on portal.* to 'portal'@'%' identified by 'portal123';
            grant all privileges on portal.* to 'portal'@'localhost' identified by 'portal123';
            flush privileges;
2. 开发须知:
    - 所有开发必须基于develop开发
    - 每次改动都必须新建分支,开发完成后将分支合并到develop分支,或者提交分支,由专门的人进行merge

# 开发工具配置
1. 首先配置git的参数

        git config --global user.name "你的名字"
        git config --global user.email "你的Email"
        #提交检出均不转换
        git config --global core.autocrlf false
        #拒绝提交包含混合换行符的文件
        git config --global core.safecrlf true
2. 配置开发工具[Eclipse]
    - 修改IDE换行符为unix格式。

        Eclipse中修改workspace配置new text file line delimiter为unix。
    - 把现有文件换成unix格式换行符。

        Eclipse中现在选择package视图中选中项目，然后选择菜单File-->Convert line delimiter to-->unix
3. 配置开发工具[Idea]
    - 修改IDE换行符为unix格式。

        Setting-->Editor-->Code Style-->Line separator:Unix and OS X(\n)
    - 把现有文件换成unix格式换行符。

        选中项目根路径,File-->Line separators-->LF-Unix and OS X(\n)
    - 配置Tomcat时，添加Deployment时application context设置为：/portal