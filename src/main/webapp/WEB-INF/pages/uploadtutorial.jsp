<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp" %>

<script>
    function start() {
        var createTutorialUrl = "/darfoobackend/rest/resources/tutorial/create";
        $.ajax({
            type: "POST",
            url: createTutorialUrl,
            data: $("#createtutorialform").serialize(),
            success: function (data) {
                if (data == "200") {
                    alert("提交教程信息成功");
                    window.location.href = "/darfoobackend/rest/resources/tutorialresource/new"
                } else if (data == "503") {
                    alert("这个作者已经有相同标题的舞蹈教程了，请修改舞蹈教程标题");
                } else if (data == "502") {
                    alert("相同名字的图片已经存在了，请修改上传图片的名字");
                } else if (data == "501") {
                    alert("教程的作者还不存在，请先创建教程作者");
                } else if (data == "508") {
                    alert("请填写并上传舞蹈教程相关的图片");
                } else {
                    alert("提交教程信息失败");
                }
            },
            error: function () {
                alert("提交教程信息失败");
            }
        })
    }

    $(function () {
        $.ajax({
            url: '/darfoobackend/rest/resources/music/all/service',
            type: 'GET',
            //beforeSend: function(xhr) {xhr.setRequestHeader('X-CSRF-Token', $('meta[name="csrf-token"]').attr('content'))},
            data: {},
            success: function (response) {
                console.log(response);

                var states = new Bloodhound({
                    datumTokenizer: function (d) {
                        return Bloodhound.tokenizers.whitespace(d.word);
                    },
                    queryTokenizer: Bloodhound.tokenizers.whitespace,
                    limit: 4,
                    local: response
                });

                states.initialize();

                $('input.typeahead-only').typeahead(null, {
                    name: 'states',
                    displayKey: 'word',
                    source: states.ttAdapter()
                });
            }
        });
    });
</script>

<div class="container">
    <h1>填写教学视频信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createtutorialform" name="createtutorialform">
                <div class="form-group">
                    <label for="title">教学视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入教学视频标题">
                </div>

                <!--<div class="form-group">
                    <label for="authorname">教学视频作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="请输入教学视频作者名字">
                </div>-->

                <div class="form-group">
                    <label for="authorname">舞蹈教学关联的明星舞队</label>
                    <select data-toggle="select" name="authorname" id="authorname"
                            class="form-control select select-success mrs mbm">
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.name}">${author.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="imagekey">教学视频封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" name="imagekey" id="imagekey" placeholder="请输入教学视频封面图片名称">
                </div>

                <div class="form-group">
                    <label for="videotype">舞蹈教程文件格式</label>
                    <select data-toggle="select" name="videotype" id="videotype"
                            class="form-control select select-success mrs mbm">
                        <option value="mp4">mp4</option>
                        <option value="flv">flv</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videospeed">教学视频速度</label>
                    <select data-toggle="select" name="videospeed" id="videospeed"
                            class="form-control select select-success mrs mbm">
                        <option value="快">快</option>
                        <option value="中">中</option>
                        <option value="慢">慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videodifficult">教学视频难度</label>
                    <select data-toggle="select" name="videodifficult" id="videodifficult"
                            class="form-control select select-success mrs mbm">
                        <option value="简单">简单</option>
                        <option value="适中">适中</option>
                        <option value="稍难">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videostyle">教学视频类型</label>
                    <select data-toggle="select" name="videostyle" id="videostyle"
                            class="form-control select select-success mrs mbm">
                        <option value="队形表演">队形表演</option>
                        <option value="背面教学">背面教学</option>
                        <option value="分解教学">分解教学</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="connectmusic">教程要关联的伴奏(没有可以暂时不填)</label>
                    <input class="form-control typeahead-only input-lg" name="connectmusic" type="text"
                           id="connectmusic" placeholder="请输入要关联的伴奏并选择"/>
                </div>

                <button type="button" class="btn btn-default" onclick="start()">提交教学视频信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp" %>