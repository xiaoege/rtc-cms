<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>ComCheck后台管理</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <style>
        .buttonclass {
            margin-top: 10px;
            margin-left: 15px;
            margin-bottom: 15px;
        }

        .layui-btn-primary {
            float: right;
            margin-right: 20px;
        }
    </style>
</head>
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">ComCheck后台管理</div>
    </div>
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree" lay-filter="test">
                <li class="layui-nav-item"><a href="/news/initNews">新闻管理</a></li>
            </ul>
        </div>
    </div>

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div class="buttonclass">
            <button type="button" class="layui-btn examinate">审核</button>
            <button type="button" class="layui-btn layui-btn-normal">保存修改</button>
            <button type="button" class="layui-btn layui-btn-primary">返回</button>
        </div>
        <div class="layui-form">
            <table class="layui-table">
                <colgroup>
                    <col width="150">
                    <col>
                </colgroup>
                <tr>
                    <td>examination</td>
                    <td id="examination"></td>
                </tr>
            </table>
            <div class="layui-form-item">
                <label class="layui-form-label">id</label>
                <div class="layui-input-block">
                    <input id="news-id" type="text" autocomplete="off"
                           class="layui-input" disabled="disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">title</label>
                <div class="layui-input-block">
                    <input id="title" type="text" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">author</label>
                <div class="layui-input-block">
                    <input id="author" type="text" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">country</label>
                <div class="layui-input-block">
                    <input id="country" type="text" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">source</label>
                <div class="layui-input-block">
                    <input id="source" type="text" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">preview</label>
                <div class="layui-input-block">
                    <input id="preview" type="text" class="layui-input" disabled="disabled">
                </div>
            </div>
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">description</label>
                <div class="layui-input-block">
                    <textarea id="description" class="layui-textarea"></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">create_time</label>
                <div class="layui-input-block">
                    <input id="create_time" type="text" autocomplete="off" class="layui-input" disabled="disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">modified_time</label>
                <div class="layui-input-block">
                    <input id="modified_time" type="text" autocomplete="off" class="layui-input" disabled="disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">content</label>
                <div class="layui-input-block" id="news-content">

                </div>
            </div>
        </div>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        © rtc
    </div>
</div>
<body class="layui-layout-body">
<script src="/static/layui/layui.js"></script>

<script>
    var newsId;

    function request(strParame) {
        var args = new Object();
        var query = location.search.substring(1);

        var pairs = query.split("&"); // Break at ampersand
        for (var i = 0; i < pairs.length; i++) {
            var pos = pairs[i].indexOf('=');
            if (pos == -1) continue;
            var argname = pairs[i].substring(0, pos);
            var value = pairs[i].substring(pos + 1);
            value = decodeURIComponent(value);
            args[argname] = value;
        }
        return args[strParame];
    }

    //JavaScript代码区域
    layui.use('element', function () {
        var element = layui.element;
        var $ = layui.$;
        let id = request("id");
        $.ajax({
            url: '/news/getNews',
            data: {id: id},
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                console.log(data)
                if (data.code == 200) {
                    let news = data.data;
                    newsId = news.id;

                    if (news.examination == 0) {
                        $('#examination').html('<span style = "color: #393d49;" > 未审核 </span>')
                    } else if (news.examination == 1) {
                        $('#examination').html('<span style = "color: #2fa7ff;" > 审核通过 </span>')
                    } else if (news.examination == 2) {
                        $('#examination').html('<span style = "color: #ff2f2f;" > 审核不通过 </span>')
                    }

                    $("#news-id").val(news.id)
                    $("#title").val(news.title)
                    $('#author').val(news.author)
                    $('#country').val(news.country)
                    $('#source').val(news.source)
                    $('#preview').val(news.preview)
                    $('#description').val(news.description)
                    $('#create_time').val(news.gmtCreate)
                    $('#modified_time').val(news.gmtModify)
                    if (news.resultList != null && news.resultList.length > 0) {
                        let resultList = news.resultList;
                        for (let i = 0; i < resultList.length; i++) {
                            let content = resultList[i]
                            if (content.type == 'content') {
                                $('#news-content').append('<textarea type="text" autocomplete="off" class="layui-textarea news-content">' + content.data + '</textarea>')
                            } else if (content.type == 'img') {
                                $('#news-content').append('<input type="text" disabled="disabled" class="layui-input news-content" value="' + content.url + '">')
                            } else {
                                $('#news-content').append('<textarea type="text" autocomplete="off" class="layui-textarea news-content">' + content.data + '</textarea>')
                            }
                        }
                    }
                } else {
                    alert('查看/编辑详情失败')
                    window.location.href = "/news/initNews"
                }
            },
            error: function (data) {
                alert('请求失败')
                window.location.href = "/news/initNews"
            }
        })

        $('.layui-btn-normal').click(function () {
            if (confirm('确认保存?')) {
                let content = []
                $('.news-content').each(function () {
                    content.push($(this).val())
                })
                let body = {
                    id: $("#news-id").val(),
                    title: $("#title").val(),
                    author: $('#author').val(),
                    country: $('#country').val(),
                    source: $('#source').val(),
                    preview: $('#preview').val(),
                    description: $('#description').val(),
                    content: content
                }
                $.ajax({
                    url: '/news/modifyNews',
                    // data: {body: JSON.stringify(body)},
                    data: JSON.stringify(body),
                    type: 'POST',
                    contentType: 'application/json',
                    success: function (data) {

                    },
                    error: function (data) {

                    }
                })
            }
        });

        $('.examinate').click(function () {
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.confirm('审核文章id：' + newsId, {icon: 3, title: '提示', btn: ['通过', '不通过', '取消']},
                    function (index) {
                        let operation = {
                            id: [newsId],
                            operation: 'approve'
                        };
                        //向服务端发送删除指令
                        $.ajax({
                            url: '/news/examinateNews',
                            data: JSON.stringify(operation),
                            type: 'POST',
                            contentType: 'application/json',
                            success: function () {
                                window.location.href = "/news/toNewsEdit?id=" + newsId;
                            },
                            error: function () {

                            }
                        })

                    },
                    function (index) {
                        let operation = {
                            id: [newsId],
                            operation: 'disapprove'
                        };
                        $.ajax({
                            url: '/news/examinateNews',
                            data: JSON.stringify(operation),
                            type: 'POST',
                            contentType: 'application/json',
                            success: function () {
                                window.location.href = "/news/toNewsEdit?id=" + newsId;
                            },
                            error: function () {

                            }
                        })
                    });
            });
        });

        $('.layui-btn-primary').click(function () {
            window.location.href = "/news/initNews"
        });
    });


</script>

</body>
</html>