<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<script>
    $(function(){
        var beat = $("#beat").text();
        var style = $("#style").text();

        $('#musicbeat option[value="' + beat + '"]').attr("selected", true);
        $('#musicstyle option[value="' + style + '"]').attr("selected", true);
    });

    function update(){
        var createmusicUrl = "/darfoobackend/rest/admin/music/updatenopic";
        $.ajax({
            type : "POST",
            url : createmusicUrl,
            data : $("#createmusicform").serialize(),
            success : function(data){
                if(data == "200"){
                    alert("更新伴奏信息成功");
                    window.location.href = "/darfoobackend/rest/admin/music/all"
                }else if(data == "505"){
                    alert("请确保舞蹈伴奏首字母填写的是一个不区分大小写的英文字母");
                }else if(data == "501"){
                    alert("该作者已经有相同名字的舞蹈伴奏了");
                }else if(data == "508"){
                    alert("请填写并上传舞蹈伴奏相关的图片");
                }else{
                    alert("更新伴奏信息失败");
                }
            },
            error : function(){
                alert("更新伴奏信息失败");
            }
        })
    }

    function kickout(){
        var musicid = $("#musicid").text();

        var targeturl = "/darfoobackend/rest/admin/music/delete";

        $.ajax({
            type : "POST",
            url : targeturl,
            data : {"id":musicid},
            success : function(data){
                if(data == "200"){
                    alert("删除伴奏信息成功");
                    window.location.href = "/darfoobackend/rest/admin/music/all"
                }else if(data == "505"){
                    alert("删除伴奏信息失败");
                }else{
                    alert("删除伴奏信息失败");
                }
            },
            error : function(){
                alert("删除伴奏信息失败");
            }
        })
    }

    function updateimage(){
        window.location.href = "/darfoobackend/rest/admin/music/updateimage/" + $("#musicid").text();
    }
</script>

<div id="musicid" style="display: none">${music.id}</div>
<div id="beat" style="display: none">${beat}</div>
<div id="style" style="display: none">${style}</div>

<div class="container">
    <h1>查看与修改舞蹈伴奏信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createmusicform" name="createmusicform">
                <div style="display: none">
                    <input type="text" name="id" value="${music.id}">
                </div>

                <div style="display: none">
                    <input type="text" name="origintitle" value="${music.title}">
                </div>

                <div class="form-group">
                    <label for="title">舞蹈伴奏标题(也就是上传伴奏文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="${music.title}">
                </div>

                <div style="display: none">
                    <input type="text" name="authorobjectname" value="${music.author.name}">
                </div>

                <div style="display: none">
                    <input type="text" name="originauthorname" value="${music.authorName}">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈伴奏作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="${music.authorName}">
                </div>

                <div style="display: none">
                    <input type="text" name="imagekey" value="${music.image.image_key}">
                </div>

                <!--<div class="form-group">
                    <label for="imagekey">舞蹈伴奏封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" id="imagekey" placeholder="${music.image.image_key}" disabled="disabled">
                </div>-->

                <div class="form-group">
                    <label for="musicbeat">舞蹈速度---<div style="color: green; display: inline; font-size: 18pt">原本为${beat}</div></label>
                    <select data-toggle="select" name="musicbeat" id="musicbeat" class="form-control select select-success mrs mbm">
                        <option value="四拍">四拍</option>
                        <option value="八拍">八拍</option>
                        <option value="十六拍">十六拍</option>
                        <option value="三十二拍">三十二拍</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="musicstyle">舞蹈风格---<div style="color: green; display: inline; font-size: 18pt">原本为${style}</div></label>
                    <select data-toggle="select" name="musicstyle" id="musicstyle" class="form-control select select-success mrs mbm">
                        <option value="草原风">草原风</option>
                        <option value="儿歌风">儿歌风</option>
                        <option value="情歌风">情歌风</option>
                        <option value="红歌风">红歌风</option>
                        <option value="戏曲风">戏曲风</option>
                        <option value="印巴风">印巴风</option>
                        <option value="江南风">江南风</option>
                        <option value="民歌风">民歌风</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="musicletter">伴奏首字母(大小写均可)</label>
                    <input type="text" class="form-control" name="musicletter" id="musicletter" value="${letter}">
                </div>

                <!--<div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>-->

                <button type="button" class="btn btn-default" onclick="update()">更新舞蹈伴奏信息</button>
                <button type="button" class="btn btn-default" onclick="kickout()">删除舞蹈伴奏</button>
                <!--<button type="button" class="btn btn-default" onclick="updateimage()">更新舞蹈伴奏封面图片</button>-->
            </form>
        </div>
    </div>
</div>


<%@include file="footer.jsp"%>
