<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>ComCheck后台管理</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
</head>
<div class="layui-layout layui-layout-admin">
    <%@ include file="/WEB-INF/jsp/bar.jsp" %>

    <script type="text/html" id="toolhead">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-sm" lay-event="getCheckData">审核</button>
            <button class="layui-btn layui-btn-sm" lay-event="getCheckLength">获取选中数目</button>
        </div>
    </script>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
        <%--        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="examination">审核</a>--%>
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
    </script>

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">
            <table class="layui-hide" id="test" lay-filter="test1"></table>
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
        var element = layui.element;
        var $ = layui.$;
    });

    layui.use('table', function () {
        var table = layui.table;
        var $ = layui.$;
        table.render({
            elem: '#test'
            , url: "/news/listNews"
            , limit: 20
            , limits: [20, 30, 40, 50]
            , toolbar: '#toolhead' //开启头部工具栏，并为其绑定左侧模板
            , cols: [[
                {type: 'checkbox', fixed: 'left'}
                , {field: 'id', title: 'ID', width: 100}
                , {field: 'title', title: '标题'}
                , {field: 'source', title: '新闻来源', width: 120}
                , {field: 'country', title: '国家'}
                , {field: 'examination', title: '审核状态', width: 100, templet: '#examination'}
                , {field: 'gmtCreate', title: '创建时间', width: 170}
                , {field: 'gmtModify', title: '修改时间', width: 170}
                , {field: 'operation', title: '操作', toolbar: '#barDemo'}
            ]]
            , page: true
        });

        //头工具栏事件
        table.on('toolbar(test1)', function (obj) {
            let checkStatus = table.checkStatus(obj.config.id);
            let data = checkStatus.data;
            switch (obj.event) {
                case 'getCheckData':
                    let idArray = [];
                    for (let i = 0; i < data.length; i++) {
                        idArray.push(data[i].id)
                    }
                    if (idArray.length != 0) {
                        layer.confirm('审核' + idArray.length + '个新闻', {icon: 3, title: '审核', btn: ['通过', '不通过', '取消']},
                            function (index) {
                                let operation = {
                                    id: idArray,
                                    operation: 'approve'
                                };
                                // layer.close(index);
                                //向服务端发送删除指令
                                $.ajax({
                                    url: '/news/examinateNews',
                                    data: JSON.stringify(operation),
                                    type: 'POST',
                                    contentType: 'application/json',
                                    success: function () {
                                        table.reload('test', {
                                            url: "/news/listNews"
                                            , where: {} //设定异步数据接口的额外参数
                                            //,height: 300
                                        });
                                        layer.close(index);
                                    },
                                    error: function () {
                                        layer.msg('请求失败')
                                        layer.close(index);
                                    }
                                })
                            },
                            function (index) {
                                let operation = {
                                    id: idArray,
                                    operation: 'disapprove'
                                };
                                $.ajax({
                                    url: '/news/examinateNews',
                                    data: JSON.stringify(operation),
                                    type: 'POST',
                                    contentType: 'application/json',
                                    success: function () {
                                        table.reload('test', {
                                            url: "/news/listNews"
                                            , where: {} //设定异步数据接口的额外参数
                                            //,height: 300
                                        });
                                        layer.close(index);
                                    },
                                    error: function () {
                                        layer.msg('请求失败')
                                        layer.close(index);
                                    }
                                })
                            });
                    }
                    break;
                case 'getCheckLength':
                    layer.msg('选中了：' + data.length + ' 个');
                    break;
            }
            ;
        });

        //监听行工具事件
        table.on('tool(test1)', function (obj) { //注：tool 是工具条事件名，test 是 table 原始容器的属性 lay-filter="对应的值"
            let data = obj.data //获得当前行数据
                , layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'detail') {
                window.location.href = "/news/toNewsDetail?id=" + obj.data.id;
            } else if (layEvent === 'edit') {
                window.location.href = "/news/toNewsEdit?id=" + obj.data.id;
            } else if (layEvent === 'examination') {
                layer.confirm('审核文章id：' + obj.data.id, {icon: 3, title: '提示', btn: ['通过', '不通过', '取消']},
                    function (index) {
                        obj.del(); //删除对应行（tr）的DOM结构
                        let data = obj.data;
                        let operation = {
                            id: data.id,
                            operation: 'approve'
                        };
                        // layer.close(index);
                        $.ajax({
                            url: '/news/examinateNews',
                            data: JSON.stringify(operation),
                            type: 'POST',
                            contentType: 'application/json',
                            success: function () {
                                table.reload('test', {
                                    url: "/news/listNews"
                                    , where: {} //设定异步数据接口的额外参数
                                    //,height: 300
                                });
                                layer.close(index);
                            },
                            error: function () {

                            }
                        })

                    },
                    function (index) {
                        // layer.close(index);
                        let data = obj.data;
                        let operation = {
                            id: data.id,
                            operation: 'disapprove'
                        };
                        $.ajax({
                            url: '/news/examinateNews',
                            data: JSON.stringify(operation),
                            type: 'POST',
                            contentType: 'application/json',
                            success: function () {

                            },
                            error: function () {

                            }
                        })
                    });
            } else if (layEvent === 'del') {
                layer.confirm('删除文章id: ' + obj.data.id, {icon: 2}, function (index) {
                    obj.del(); //删除对应行（tr）的DOM结构
                    layer.close(index);
                    //向服务端发送删除指令
                    $.ajax({
                        url: '/news/removeNews',
                        data: {id: obj.data.id},
                        type: 'POST',
                        success: function (data) {
                            if (data.code == 200) {
                                layer.msg('删除成功');
                            } else {
                                layer.msg('删除失败')
                            }
                        },
                        error: function () {
                            layer.msg('请求失败')
                        }
                    })
                });
            }
        });
    });


</script>

<script type="text/html" id="examination">
    {{#  if(d.examination == 0){ }}
    <span style="color: #393d49;">未审核</span>
    {{#  } else if(d.examination == 1){ }}
    <span style="color: #2fa7ff;">审核通过</span>
    {{#  } else if(d.examination == 2){ }}
    <span style="color: #ff2f2f;">审核不通过</span>
    {{#  } }}
</script>
</body>
</html>