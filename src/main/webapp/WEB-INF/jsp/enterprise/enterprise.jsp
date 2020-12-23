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
        .rtc-date-label {
            width: 100px !important;
        }

        .rtc-date-div {
            margin-top: 10px;
            margin-bottom: 10px;
        }

        .query-button {
            margin-left: 50px;
        }

    </style>
</head>

<body class="layui-layout-body">

<div class="layui-layout layui-layout-admin">
    <%@ include file="/WEB-INF/jsp/bar.jsp" %>
    <script type="text/html" id="toolhead">
        <div>
            <div class="layui-input-inline">
                <input id="data-name" type="text" required lay-verify="required" placeholder="请输入公司名"
                       autocomplete="off" class="layui-input" value="">
            </div>
            <div class="layui-input-inline">
                <select id="data-nation" lay-filter="nation">
                    <option></option>
                    <option value="America">America</option>
                    <option value="China">China</option>
                    <option value="Vietnam">Vietnam</option>
                    <option value="Canada">Canada</option>
                    <option value="India">India</option>
                </select>
            </div>
            <div class="layui-input-inline">
                <select id="data-area" lay-verify="">
                    <option></option>
                </select>
            </div>
            <div class="layui-input-inline query-button">
                <button class="layui-btn layui-btn-sm" onclick="getEnterprise()">查询</button>
                <button class="layui-btn layui-btn-sm" onclick="clearQuery()">重置</button>
            </div>
        </div>
        <div class="layui-inline rtc-date-div">
            <label class="layui-form-label rtc-date-label">创建时间</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="data-start-date" autocomplete="off" placeholder="开始时间">
            </div>
            <div class="layui-inline">
                -
            </div>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" id="data-end-date" autocomplete="off" placeholder="结束时间">
            </div>
        </div>
        <div>
            <div class="layui-btn-container">
                <button class="layui-btn layui-btn-sm" lay-event="getCheckData">删除</button>
                <button class="layui-btn layui-btn-sm" lay-event="getCheckLength">获取选中数目</button>
            </div>
        </div>
    </script>

    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
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
<script src="/layui/layui.js"></script>
<script src="/js/nation.js"></script>

<script>
    //JavaScript代码区域
    layui.use('element', function () {
        let element = layui.element;
        let $ = layui.$;
    });

    layui.use('laydate', function () {
        let laydate = layui.laydate;
        let $ = layui.$;
        //执行一个laydate实例
        let startDate;
        let endDate;
        laydate.render({
            elem: '#data-start-date' //指定元素
            , trigger : 'click'
        });

        laydate.render({
            elem: '#data-end-date'
            , trigger : 'click'
        });
    });

    layui.use('table', function () {
        let table = layui.table;
        let $ = layui.$;
        table.render({
            elem: '#test'
            , url: '/enterprise/listEnterprise'
            , limit: 20
            , limits: [20, 30, 40, 50]
            , toolbar: '#toolhead' //开启头部工具栏，并为其绑定左侧模板
            , cols: [[
                {type: 'checkbox', fixed: 'left'}
                , {field: 'pid', title: 'ID', width: 100}
                , {field: 'eName', title: '公司', width: 250}
                , {field: 'address', title: '地址', width: 200}
                , {field: 'nation', title: '国家', width: 140}
                , {field: 'eType', title: '地区', width: 140}
                , {field: 'createTime', title: '创建时间', width: 200}
                , {field: 'operation', title: '操作', toolbar: '#barDemo', width: 180}
            ]]
            , page: true
        });

        //头工具栏事件
        table.on('toolbar(test1)', function (obj) {
            let checkStatus = table.checkStatus(obj.config.id);
            let data = checkStatus.data;
            switch (obj.event) {
                case 'getCheckData':
                    let companyArray = [];
                    for (let i = 0; i < data.length; i++) {
                        let company = {
                            id: data[i].pid,
                            esId: data[i].esId,
                            nation: data[i].nation,
                            eType: data[i].eType
                        }
                        companyArray.push(company)
                    }
                    if (companyArray.length != 0) {
                        layer.confirm('删除' + data.length + '个公司', {icon: 3, title: '删除', btn: ['删除', '取消']},
                            function (index) {
                                $.ajax({
                                    url: '/enterprise/removeEnterpriseList',
                                    data: JSON.stringify(companyArray),
                                    type: 'POST',
                                    contentType: 'application/json',
                                    success: function () {
                                        layer.close(index);
                                        let queryData = getQueryData();
                                        table.reload('test', {
                                            url: '/enterprise/listEnterprise'
                                            , where: queryData
                                            , done:function(){
                                                layui.laydate.render({
                                                    elem: '#data-start-date'
                                                    ,trigger : 'click'
                                                });
                                                layui.laydate.render({
                                                    elem: '#data-end-date'
                                                    ,trigger : 'click'
                                                });
                                            }
                                        });
                                        resetQueryData(queryData)
                                    },
                                    error: function () {
                                        layer.close(index);
                                        layer.msg('请求失败')
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
                // window.location.href = "/news/toNewsDetail?id=" + obj.data.pid;
            } else if (layEvent === 'edit') {
                // window.location.href = "/news/toNewsEdit?id=" + obj.data.pid;
            } else if (layEvent === 'del') {
                layer.confirm('删除公司id: ' + obj.data.pid, {icon: 3}, function (index) {
                    // obj.del(); //删除对应行（tr）的DOM结构
                    $.ajax({
                        url: '/enterprise/removeEnterprise',
                        data: {id: obj.data.pid, esId: obj.data.esId, nation: obj.data.nation, eType: obj.data.eType},
                        type: 'POST',
                        success: function (data) {
                            if (data.code == 200) {
                                layer.close(index);
                                layer.msg('删除成功');
                                let queryData = getQueryData();
                                table.reload('test', {
                                    url: '/enterprise/listEnterprise'
                                    , where: queryData
                                    , done:function(){
                                        layui.laydate.render({
                                            elem: '#data-start-date'
                                            ,trigger : 'click'
                                        });
                                        layui.laydate.render({
                                            elem: '#data-end-date'
                                            ,trigger : 'click'
                                        });
                                    }
                                });
                                resetQueryData(queryData)
                            } else {
                                layer.close(index);
                                layer.msg('删除失败')
                            }
                        },
                        error: function () {
                            layer.close(index);
                            layer.msg('请求失败')
                        }
                    })
                });
            }
        });
    });

    layui.use('form', function () {
        let form = layui.form;
        let element = layui.element;
        let $ = layui.$;
        form.on('select(nation)', function (data) {
            $('#data-area').empty()
            let selected = data.value
            for (let i = 0; i < nation_area.length; i++) {
                if (selected == nation_area[i].country) {
                    for (let j = 0; j < nation_area[i].area.length; j++) {
                        let option = '<option value="' + nation_area[i].area[j].idx + '">' + nation_area[i].area[j].name + '</option>'
                        $('#data-area').append(option)
                    }
                }
            }
            form.render()
        });


    });

    function getEnterprise() {
        let element = layui.element;
        let $ = layui.$;
        let data = getQueryData();

        layui.use('table', function () {
            let table = layui.table;
            table.reload('test', {
                url: '/enterprise/listEnterprise',
                where: data,
                page: {
                    curr: 1
                },
                done:function(){
                    layui.laydate.render({
                        elem: '#data-start-date'
                        ,trigger : 'click'
                    });
                    layui.laydate.render({
                        elem: '#data-end-date'
                        ,trigger : 'click'
                    });
                }
            });
        })
        resetQueryData(data);
    };

    // 获得搜索条件
    function getQueryData() {
        let element = layui.element;
        let $ = layui.$;
        let data = {
            name: $('#data-name').val(),
            nation: $('#data-nation').val(),
            area: $('#data-area').val(),
            start_date: $('#data-start-date').val(),
            end_date: $('#data-end-date').val()
        }

        return data;
    }

    // 带条件的搜索过后保留条件
    function resetQueryData(data) {
        let element = layui.element;
        let $ = layui.$;
        $('#data-name').val(data.name)
        $('#data-nation').val(data.nation)
        $('#data-start-date').val(data.start_date)
        $('#data-end-date').val(data.end_date)

        // 填充地区的下拉框
        let selected = data.nation
        for (let i = 0; i < nation_area.length; i++) {
            if (selected == nation_area[i].country) {
                for (let j = 0; j < nation_area[i].area.length; j++) {
                    let option = '<option value="' + nation_area[i].area[j].idx + '">' + nation_area[i].area[j].name + '</option>'
                    $('#data-area').append(option)
                }
            }
        }

        $('#data-area').val(data.area)
    }

    // 重置搜索条件
    function clearQuery() {
        let element = layui.element;
        let $ = layui.$;
        $('input').val('')
        $('option:selected').attr("selected", false);
    }
</script>

</body>
</html>