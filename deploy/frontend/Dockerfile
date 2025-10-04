# 直接使用 Nginx 基础镜像，不需要 Node 环境
FROM nginx:alpine

# 把本地的 nginx.conf 复制到容器的 Nginx 配置目录
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 把本地已打包好的 dist 文件夹，复制到 Nginx 的默认静态文件目录
COPY dist /usr/share/nginx/html

# 暴露 80 端口
EXPOSE 80

# Nginx 容器默认会启动，不需要额外的 ENTRYPOINT