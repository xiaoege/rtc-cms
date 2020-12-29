<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>ComChec Management</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <style>
        .buttonclass {
            margin-top: 10px;
            margin-left: 15px;
        }

        .layui-btn-primary {
            float: right;
            margin-right: 20px;
        }
    </style>
</head>
<div class="layui-layout layui-layout-admin">
    <%@ include file="/META-INF/resources/bar.jsp" %>

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div class="buttonclass">
            <button type="button" class="layui-btn examinate">Examine</button>
            <button type="button" class="layui-btn layui-btn-normal">Edit</button>
            <button type="button" class="layui-btn layui-btn-primary">Back</button>
        </div>
        <div class="layui-form">
            <table class="layui-table">
                <colgroup>
                    <col width="150">
                    <col>
                </colgroup>
                <thead>
                <tr>
                    <th></th>
                    <th>Content</th>
                </tr>
                </thead>
                <tbody id="news-tbody">
                <tr>
                    <td>id</td>
                    <td id="news-id">
                </tr>
                <tr>
                    <td>examination</td>
                    <td id="examination">
                </tr>
                <tr>
                    <td>title</td>
                    <td id="title">
                </tr>
                <tr>
                    <td>author</td>
                    <td id="author">
                </tr>
                <tr>
                    <td>country</td>
                    <td id="country">
                </tr>
                <tr>
                    <td>source</td>
                    <td id="source">
                </tr>
                <tr>
                    <td>preview</td>
                    <td id="preview">
                </tr>
                <tr>
                    <td>description</td>
                    <td id="description">
                </tr>
                <tr>
                    <td>create_time</td>
                    <td id="gmtCreate">
                </tr>
                <tr>
                    <td>modified_time</td>
                    <td id="gmtModify">
                </tr>
                <tr>
                    <td>content</td>
                    <td>
                </tr>
                </tbody>
            </table>
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
                    $('#title').html("<p>" + news.title + "</p>")
                    $('#news-id').html(news.id)
                    if (news.examination == 0) {
                        $('#examination').html('<span style = "color: #393d49;" > Pending </span>')
                    } else if (news.examination == 1) {
                        $('#examination').html('<span style = "color: #2fa7ff;" > Approved </span>')
                    } else if (news.examination == 2) {
                        $('#examination').html('<span style = "color: #ff2f2f;" > Disapproved </span>')
                    }
                    $('#author').text(news.author)
                    $('#country').text(news.country)
                    $('#source').text(news.source)
                    $('#preview').html('<a target="_blank" href="' + news.preview + '">' + news.preview + '</a>')
                    $('#description').text(news.description)
                    $('#gmtCreate').text(news.gmtCreate)
                    $('#gmtModify').text(news.gmtModify)
                    if (news.resultList != null && news.resultList.length > 0) {
                        let resultList = news.resultList;
                        for (let i = 0; i < resultList.length; i++) {
                            let content = resultList[i]
                            if (content.type == 'content') {
                                $('#news-tbody').append('<tr><td></td><td>' + content.data + '</td>')
                            } else if (content.type == 'img') {
                                $('#news-tbody').append('<tr><td/><td><a target="_blank" href="' + content.url + '">' + content.url + '</a>' + '</td>')
                            } else {
                                $('#news-tbody').append('<tr><td>other style</td><td>' + content.data + '</td>')
                            }
                        }
                    }
                } else {
                    layer.msg('Bad request.')
                    window.location.href = "/news/initNews"
                }
            },
            error: function (data) {
                layer.msg('Bad request.')
                window.location.href = "/news/initNews"
            }
        })


        $('.examinate').click(function () {
            layui.use('layer', function () {
                var layer = layui.layer;
                layer.confirm('Examine news id：' + newsId, {icon: 3, title: 'Examine', btn: ['Approve', 'Disapprove', 'Cancel']},
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
                                layer.msg('Approve completed.')
                                window.location.href = "/news/toNewsDetail?id=" + newsId;
                            },
                            error: function () {
                                layer.msg('Approve failed.');
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
                                layer.msg('Disapprove completed.')
                                window.location.href = "/news/toNewsDetail?id=" + newsId;
                            },
                            error: function () {
                                layer.msg('Disapprove failed.');
                            }
                        })
                    });
            });
        });


        $('.layui-btn-normal').click(function () {
            window.location.href = "/news/toNewsEdit?id=" + newsId;
        });

        $('.layui-btn-primary').click(function () {
            window.location.href = "/news/initNews"
        });
    })
    ;
</script>

</body>
</html>