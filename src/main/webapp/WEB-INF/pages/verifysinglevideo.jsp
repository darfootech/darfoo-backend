<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="header.jsp"%>

<script>
    function start(){
        var createVideoUrl = "/darfoobackend/rest/resources/video/create";
        $.ajax({
            type : "POST",
            url : createVideoUrl,
            data : $("#createvideoform").serialize(),
            success : function(data){
                if(data == "200"){
                    alert("提交视频信息成功");
                    window.location.href = "/darfoobackend/rest/resources/videoresource/new"
                }else if(data == "505"){
                    alert("请确保舞蹈视频首字母填写的是一个不区分大小写的英文字母");
                }else if(data == "503"){
                    alert("这个作者已经有相同标题的舞蹈视频了，请修改舞蹈视频标题");
                }else if(data == "502"){
                    alert("相同名字的图片已经存在了，请修改上传图片的名字");
                }else if(data == "501"){
                    alert("舞蹈视频的作者还不存在，请先创建舞蹈视频作者");
                }else if(data == "508"){
                    alert("请填写并上传舞蹈视频相关的图片");
                }else{
                    alert("提交视频信息失败");
                }
            },
            error : function(){
                alert("提交视频信息失败");
            }
        })
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

<div class="container">
    <h1>填写舞蹈视频信息</h1>
    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createvideoform" name="createvideoform">
                <div class="form-group">
                    <label for="title">舞蹈视频标题(也就是上传视频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入舞蹈视频标题">
                </div>

                <!--<div class="form-group">
                    <label for="authorname">舞蹈视频作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="请输入舞蹈视频作者名字">
                </div>-->

                <div class="form-group">
                    <label for="authorname">舞蹈视频关联的明星舞队</label>
                    <select data-toggle="select" name="authorname" id="authorname" class="form-control select select-success mrs mbm">
                        <c:forEach var="author" items="${authors}">
                            <option value="${author.name}">${author.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="imagekey">舞蹈视频封面图片预览</label>
                    <img src="${imageurl}" name="imagekey" id="imagekey" width="600" height="600">
                </div>

                <div class="form-group">
                    <label for="imagekey">舞蹈视频预览</label>
                    <video id="MY_VIDEO_1" class="video-js vjs-default-skin" controls preload="auto" width="640" height="264" poster="MY_VIDEO_POSTER.jpg" data-setup="{}">
                        <source src="" type='video/'>

                        <p class="vjs-no-js">To view this video please enable JavaScript, and consider upgrading to a web browser that
                            <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
                        </p>
                    </video>
                </div>

                <div class="form-group">
                    <label for="videotype">舞蹈视频文件格式</label>
                    <select data-toggle="select" name="videotype" id="videotype" class="form-control select select-success mrs mbm">
                        <option value="mp4">mp4</option>
                        <option value="flv">flv</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videospeed">舞蹈速度</label>
                    <select data-toggle="select" name="videospeed" id="videospeed" class="form-control select select-success mrs mbm">
                        <option value="较快">较快</option>
                        <option value="适中">适中</option>
                        <option value="较慢">较慢</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videodifficult">舞蹈难度</label>
                    <select data-toggle="select" name="videodifficult" id="videodifficult" class="form-control select select-success mrs mbm">
                        <option value="简单">简单</option>
                        <option value="中等">中等</option>
                        <option value="稍难">稍难</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="videostyle">舞蹈风格</label>
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
                    <input type="text" class="form-control" name="videoletter" id="videoletter" placeholder="请输入舞蹈视频首字母,大小写均可">
                </div>

                <div class="form-group">
                    <label for="connectmusic">视频要关联的伴奏(没有可以暂时不填)</label>
                    <input class="form-control typeahead-only input-lg" name="connectmusic" type="text" id="connectmusic" placeholder="请输入要关联的伴奏并选择" />
                </div>

                <button type="button" class="btn btn-default" onclick="start()">提交舞蹈视频信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>