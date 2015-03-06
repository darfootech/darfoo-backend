<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp"%>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<script>
    $(function () {
        var speed = $("#speed").text();
        var difficult = $("#difficult").text();
        var style = $("#style").text();
        var videotype = $("#oldvideotype").text();
        var authorname = $("#oldauthorname").text();

        $('#tutorialspeed option[value="' + speed + '"]').attr("selected", true);
        $('#tutorialdifficult option[value="' + difficult + '"]').attr("selected", true);
        $('#tutorialstyle option[value="' + style + '"]').attr("selected", true);
        $('#videotype option[value="' + videotype + '"]').attr("selected", true);
        $('#authorname option[value="' + authorname + '"]').attr("selected", true);
    });
</script>

<div id="speed" style="display: none">${speed}</div>
<div id="difficult" style="display: none">${difficult}</div>
<div id="style" style="display: none">${style}</div>
<div id="oldvideotype" style="display: none">${videotype}</div>
<div id="oldauthorname" style="display: none">${video.author.name}</div>

<div class="container">
    <h1>查看与修改舞蹈教程信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
                <div style="display: none">
                    <input type="text" name="id" value="${video.id}">
                </div>

                <div style="display: none">
                    <input type="text" name="title" value="${video.title}">
                </div>

                <div class="form-group">
                    <label for="title">舞蹈教程标题(也就是上传教程文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" id="title" placeholder="${video.title}" disabled="disabled">
                </div>

                <div class="form-group">
                    <label for="authorname">关联的明星舞队---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${video.author.name}</div>
                    </label>
                    <select data-toggle="select" name="authorname" id="authorname"
                            class="form-control select select-success mrs mbm">
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.name}">${author.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div style="display: none">
                    <input type="text" name="imagekey" value="${video.image.image_key}">
                </div>

                <div class="form-group">
                    <label for="imagekey">舞蹈教程封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" id="imagekey" placeholder="${video.image.image_key}"
                           disabled="disabled">
                </div>

                <div class="form-group">
                    <label>舞蹈教程格式---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${videotype}</div>
                    </label>
                </div>

                <div class="form-group">
                    <label for="tutorialspeed">舞蹈速度---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${speed}</div>
                    </label>
                    <select data-toggle="select" name="tutorialspeed" id="tutorialspeed"
                            class="form-control select select-success mrs mbm">
                        <option value="快">快</option>
                        <option value="中">中</option>
                        <option value="慢">慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="tutorialdifficult">舞蹈难度---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${difficult}</div>
                    </label>
                    <select data-toggle="select" name="tutorialdifficult" id="tutorialdifficult"
                            class="form-control select select-success mrs mbm">
                        <option value="简单">简单</option>
                        <option value="适中">适中</option>
                        <option value="稍难">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="tutorialstyle">舞蹈风格---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${style}</div>
                    </label>
                    <select data-toggle="select" name="tutorialstyle" id="tutorialstyle"
                            class="form-control select select-success mrs mbm">
                        <option value="队形表演">队形表演</option>
                        <option value="背面教学">背面教学</option>
                        <option value="分解教学">分解教学</option>
                    </select>
                </div>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="form-group">
                    <a href="${videourl}" target="_blank">点击此处预览对应的视频资源</a>
                </div>

                <div class="form-group">
                    <label for="connectmusic">教程要关联的伴奏(没有可以暂时不填)</label>
                    <input class="form-control typeahead-only input-lg" name="connectmusic" type="text"
                           id="connectmusic" placeholder="${connectmusic}"/>
                </div>

                <button type="button" class="btn btn-default" id="update">更新舞蹈教程信息</button>
                <button type="button" class="btn btn-default" id="kickout">删除舞蹈教程</button>
                <button type="button" class="btn btn-default" id="updateimage">更新舞蹈教程封面图片</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
