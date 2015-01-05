<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<script>
    $(function(){
        var speed = $("#speed").text();
        var difficult = $("#difficult").text();
        var style = $("#style").text();
        var type = $("#type").text();
        var starteamname = $("#starteamname").text();

        $('#videospeed option[value="' + speed + '"]').attr("selected", true);
        $('#videodifficult option[value="' + difficult + '"]').attr("selected", true);
        $('#videostyle option[value="' + style + '"]').attr("selected", true);
        $('#videotype option[value="' + type + '"]').attr("selected", true);
        $('#authorname option[value="' + starteamname + '"]').attr("selected", true);
    });

    function update(){
        var createVideoUrl = "/darfoobackend/rest/admin/video/update";
        $.ajax({
            type : "POST",
            url : createVideoUrl,
            data : $("#createvideoform").serialize(),
            success : function(data){
                if(data == "200"){
                    alert("更新视频信息成功");
                    window.location.href = "/darfoobackend/rest/admin/video/all"
                }else if(data == "505"){
                    alert("请确保舞蹈视频首字母填写的是一个不区分大小写的英文字母");
                }else if(data == "501"){
                    alert("该作者已经有相同名字的舞蹈视频了");
                }else if(data == "508"){
                    alert("请填写并上传舞蹈视频相关的图片");
                }else{
                    alert("更新视频信息失败");
                }
            },
            error : function(){
                alert("更新视频信息失败");
            }
        })
    }

    function kickout(){
        var videoid = $("#videoid").text();

        var targeturl = "/darfoobackend/rest/admin/video/delete";

        $.ajax({
            type : "POST",
            url : targeturl,
            data : {"id":videoid},
            success : function(data){
                if(data == "200"){
                    alert("删除视频信息成功");
                    window.location.href = "/darfoobackend/rest/admin/video/all"
                }else if(data == "505"){
                    alert("删除视频信息失败");
                }else{
                    alert("删除视频信息失败");
                }
            },
            error : function(){
                alert("删除视频信息失败");
            }
        })
    }

    function updateimage(){
        window.location.href = "/darfoobackend/rest/admin/video/updateimage/" + $("#videoid").text();
    }

    $(function(){
        $.ajax({
            url: '/darfoobackend/rest/resources/music/all/service',
            type: 'GET',
            //beforeSend: function(xhr) {xhr.setRequestHeader('X-CSRF-Token', $('meta[name="csrf-token"]').attr('content'))},
            data: {},
            success: function(response) {
                console.log(response);

                var states = new Bloodhound({
                    datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.word); },
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

<div id="videoid" style="display: none">${video.id}</div>
<div id="speed" style="display: none">${speed}</div>
<div id="difficult" style="display: none">${difficult}</div>
<div id="style" style="display: none">${style}</div>
<div id="type" style="display: none">${videotype}</div>
<div id="starteamname" style="display: none">${video.author.name}</div>

<div class="container">
    <h1>查看与修改舞蹈视频信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createvideoform" name="createvideoform">
                <div style="display: none">
                    <input type="text" name="id" value="${video.id}">
                </div>

                <div style="display: none">
                    <input type="text" name="origintitle" value="${video.title}">
                </div>

                <div class="form-group">
                    <label for="title">舞蹈视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="${video.title}">
                </div>

                <!--<div style="display: none">
                    <input type="text" name="authorname" value="${video.author.name}">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈视频作者名字</label>
                    <input type="text" class="form-control" id="authorname" placeholder="${video.author.name}" disabled="disabled">
                </div>-->

                <div class="form-group">
                    <label for="authorname">关联的明星舞队---<div style="color: green; display: inline; font-size: 18pt">原本为${video.author.name}</div></label>
                    <select data-toggle="select" name="authorname" id="authorname" class="form-control select select-success mrs mbm">
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.name}">${author.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div style="display: none">
                    <input type="text" name="imagekey" value="${video.image.image_key}">
                </div>

                <div class="form-group">
                    <label for="imagekey">舞蹈视频封面图片标题(也就是上传图片文件的文件名,需要加上后缀)</label>
                    <input type="text" class="form-control" id="imagekey" placeholder="${video.image.image_key}" disabled="disabled">
                </div>

                <div class="form-group">
                    <label for="videotype">舞蹈视频格式---<div style="color: green; display: inline; font-size: 18pt">原本为${videotype}</div></label>
                    <select data-toggle="select" name="videotype" id="videotype" class="form-control select select-success mrs mbm">
                        <option value="mp4">mp4</option>
                        <option value="flv">flv</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videospeed">舞蹈速度---<div style="color: green; display: inline; font-size: 18pt">原本为${speed}</div></label>
                    <select data-toggle="select" name="videospeed" id="videospeed" class="form-control select select-success mrs mbm">
                        <option value="较快">较快</option>
                        <option value="适中">适中</option>
                        <option value="较慢">较慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videodifficult">舞蹈难度---<div style="color: green; display: inline; font-size: 18pt">原本为${difficult}</div></label>
                    <select data-toggle="select" name="videodifficult" id="videodifficult" class="form-control select select-success mrs mbm">
                        <option value="简单">简单</option>
                        <option value="中等">中等</option>
                        <option value="稍难">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videostyle">舞蹈风格---<div style="color: green; display: inline; font-size: 18pt">原本为${style}</div></label>
                    <select data-toggle="select" name="videostyle" id="videostyle" class="form-control select select-success mrs mbm">
                        <option value="欢快">欢快</option>
                        <option value="活泼">活泼</option>
                        <option value="优美">优美</option>
                        <option value="情歌风">情歌风</option>
                        <option value="红歌风">红歌风</option>
                        <option value="草原风">草原风</option>
                        <option value="戏曲风">戏曲风</option>
                        <option value="印巴风">印巴风</option>
                        <option value="江南风">江南风</option>
                        <option value="民歌风">民歌风</option>
                        <option value="儿歌风">儿歌风</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videoletter">视频首字母(大小写均可)</label>
                    <input type="text" class="form-control" name="videoletter" id="videoletter" value="${letter}">
                </div>

                <div class="form-group">
                    <img src="${imageurl}" width="600" height="600">
                </div>

                <div class="form-group">
                    <label for="connectmusic">视频要关联的伴奏(没有可以暂时不填)</label>
                    <input class="form-control typeahead-only input-lg" name="connectmusic" type="text" id="connectmusic" placeholder="${connectmusic}" />
                </div>

                <button type="button" class="btn btn-default" onclick="update()">更新舞蹈视频信息</button>
                <button type="button" class="btn btn-default" onclick="kickout()">删除舞蹈视频</button>
                <button type="button" class="btn btn-default" onclick="updateimage()">更新舞蹈视频封面图片</button>
            </form>
        </div>
    </div>
</div>


<%@include file="footer.jsp"%>
