<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>

<script src="/darfoobackend/resources/js/uploadresource.js"></script>

<div id="type" style="display: none">${type}</div>

<div class="container">
    <h1>填写舞蹈伴奏信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="createresourceform">
                <div class="form-group">
                    <label for="title">舞蹈伴奏标题(也就是上传音频文件的文件名,不需要后缀)</label>
                    <input type="text" class="form-control" name="title" id="title" placeholder="请输入舞蹈伴奏标题">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈伴奏作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname" placeholder="请输入舞蹈伴奏作者名字">
                </div>

                <div class="form-group">
                    <label for="musicbeat">舞蹈伴奏节拍</label>
                    <select data-toggle="select" name="musicbeat" id="musicbeat"
                            class="form-control select select-success mrs mbm">
                        <option value="四拍">四拍</option>
                        <option value="八拍">八拍</option>
                        <option value="十六拍">十六拍</option>
                        <option value="三十二拍">三十二拍</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="musicstyle">舞蹈伴奏风格</label>
                    <select data-toggle="select" name="musicstyle" id="musicstyle"
                            class="form-control select select-success mrs mbm">
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
                    <label for="musicletter">视频首字母(大小写均可)</label>
                    <input type="text" class="form-control" name="musicletter" id="musicletter"
                           placeholder="请输入舞蹈伴奏首字母,大小写均可">
                </div>

                <button type="button" class="btn btn-default" id="start">提交舞蹈伴奏信息</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>