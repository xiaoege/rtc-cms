<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>ComChec Management</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
</head>
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">ComChec Management</div>
        <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">
            <li class="layui-nav-item" >
                <a href="/logout" onclick="if (confirm('确认退出?')==false)return false;">Sign out</a>
            </li>
        </ul>
    </div>
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree" lay-filter="test">
                <li class="layui-nav-item"><a id="bar-enterprise" href="/enterprise/initEnterprise">Enterprise</a></li>
                <li class="layui-nav-item"><a id="bar-news" href="/news/initNews">News</a></li>
            </ul>
        </div>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        © rtc
    </div>
</div>
<body class="layui-layout-body">
<script src="/layui/layui.js"></script>

<script>
    //JavaScript代码区域
    layui.use('element', function () {
        let element = layui.element;
        let $ = layui.$;
    });

</script>

</body>
</html>