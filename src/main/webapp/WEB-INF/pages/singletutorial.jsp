<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<script>
    $(function(){
        var speed = $("#speed").text();
        var difficult = $("#difficult").text();
        var style = $("#style").text();
        var type = $("#type").text();
        var starteamname = $("#starteamname").text();

        $('#tutorialspeed option[value="' + speed + '"]').attr("selected", true);
        $('#tutorialdifficult option[value="' + difficult + '"]').attr("selected", true);
        $('#tutorialstyle option[value="' + style + '"]').attr("selected", true);
        $('#videotype option[value="' + type + '"]').attr("selected", true);
        $('#authorname option[value="' + starteamname + '"]').attr("selected", true);
    });

    function update(){
        var createtutorialUrl = "/darfoobackend/rest/admin/tutorial/update";
        $.ajax({
            type : "POST",
            url : createtutorialUrl,
            data : $("#createtutorialform").serialize(),
            success : function(data){
                if(data == "200"){
                    alert("更新教程信息成功");
                    window.location.href = "/darfoobackend/rest/admin/tutorial/all"
                }else if(data == "505"){
                    alert("请确保舞蹈教程首字母填写的是一个不区分大小写的英文字母");
                }else if(data == "501"){
                    alert("该作者已经有相同名字的舞蹈教程了");
                }else{
                    alert("更新教程信息失败");
                }
            },
            error : function(){
                alert("更新教程信息失败");
            }
        })
    }

    function kickout(){
        var tutorialid = $("#tutorialid").text();

        var targeturl = "/darfoobackend/rest/admin/tutorial/delete";

        $.ajax({
            type : "POST",
            url : targeturl,
            data : {"id":tutorialid},
            success : function(data){
                if(data == "200"){
                    alert("删除教程信息成功");
                    window.location.href = "/darfoobackend/rest/admin/tutorial/all"
                }else if(data == "505"){
                    alert("删除教程信息失败");
                }else{
                    alert("删除教程信息失败");
                }
            },
            error : function(){
                alert("删除教程信息失败");
            }
        })
    }
</script>

<div id="tutorialid" style="display: none">${tutorial.id}</div>
<div id="speed" style="display: none">${speed}</div>
<div id="difficult" style="display: none">${difficult}</div>
<div id="style" style="display: none">${style}</div>
<div id="type" style="display: none">${videotype}</div>
<div id="starteamname" style="display: none">${tutorial.author.name}</div>

<div class="container">
    <h1>查看与修改舞蹈教程信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createtutorialform" name="createtutorialform">
                <div style="display: none">
                    <input type="text" name="id" value="${tutorial.id}">
                </div>

                <div style="display: none">
                    <input type="text" name="origintitle" value="${tutorial.title}">
                </div>

                <div class="form-group">
                    <label for="title">舞蹈教程标题(也就是上传教程文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="${tutorial.title}">
                </div>

                <!--<div style="display: none">
                    <input type="text" name="authorname" value="${tutorial.author.name}">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈教程作者名字</label>
                    <input type="text" class="form-control" id="authorname" placeholder="${tutorial.author.name}" disabled="disabled">
                </div>-->

                <div class="form-group">
                    <label for="authorname">关联的明星舞队---<div style="color: green; display: inline; font-size: 18pt">原本为${tutorial.author.name}</div></label>
                    <select data-toggle="select" name="authorname" id="authorname" class="form-control select select-success mrs mbm">
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.name}">${author.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div style="display: none">
                    <input type="text" name="imagekey" value="${tutorial.image.image_key}">
                </div>

                <div class="form-group">
                    <label for="imagekey">舞蹈教程封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" id="imagekey" placeholder="${tutorial.image.image_key}" disabled="disabled">
                </div>

                <div class="form-group">
                    <label for="videotype">舞蹈教程格式---<div style="color: green; display: inline; font-size: 18pt">原本为${videotype}</div></label>
                    <select data-toggle="select" name="videotype" id="videotype" class="form-control select select-success mrs mbm">
                        <option value="mp4">mp4</option>
                        <option value="flv">flv</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="tutorialspeed">舞蹈速度---<div style="color: green; display: inline; font-size: 18pt">原本为${speed}</div></label>
                    <select data-toggle="select" name="tutorialspeed" id="tutorialspeed" class="form-control select select-success mrs mbm">
                        <option value="快">快</option>
                        <option value="中">中</option>
                        <option value="慢">慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="tutorialdifficult">舞蹈难度---<div style="color: green; display: inline; font-size: 18pt">原本为${difficult}</div></label>
                    <select data-toggle="select" name="tutorialdifficult" id="tutorialdifficult" class="form-control select select-success mrs mbm">
                        <option value="简单">简单</option>
                        <option value="适中">适中</option>
                        <option value="稍难">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="tutorialstyle">舞蹈风格---<div style="color: green; display: inline; font-size: 18pt">原本为${style}</div></label>
                    <select data-toggle="select" name="tutorialstyle" id="tutorialstyle" class="form-control select select-success mrs mbm">
                        <option value="队形表演">队形表演</option>
                        <option value="背面教学">背面教学</option>
                        <option value="分解教学">分解教学</option>
                    </select>
                </div>

                <button type="button" class="btn btn-default" onclick="update()">更新舞蹈教程信息</button>
                <button type="button" class="btn btn-default" onclick="kickout()">删除舞蹈教程</button>
            </form>
        </div>
    </div>
</div>


<%@include file="footer.jsp"%>
