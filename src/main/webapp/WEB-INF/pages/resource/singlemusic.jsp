<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="../header.jsp" %>
<%@include file="../update/updatecota.jsp"%>

<script src="/darfoobackend/resources/js/modifyresource.js"></script>

<script>
    $(function () {
        var beat = $("#beat").text();
        var style = $("#style").text();

        $('#musicbeat option[value="' + beat + '"]').attr("selected", true);
        $('#musicstyle option[value="' + style + '"]').attr("selected", true);
    });
</script>

<div id="beat" style="display: none">${beat}</div>
<div id="style" style="display: none">${style}</div>

<div class="container">
    <h1>查看与修改舞蹈伴奏信息</h1>

    <div class="row">
        <div class="col-md-12">
            <form role="form" id="updateresourceform">
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
                    <input type="text" name="originauthorname" value="${music.authorname}">
                </div>

                <div class="form-group">
                    <label for="authorname">舞蹈伴奏作者名字</label>
                    <input type="text" class="form-control" name="authorname" id="authorname"
                           placeholder="${music.authorname}">
                </div>

                <div class="form-group">
                    <label for="musicbeat">舞蹈速度---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${beat}</div>
                    </label>
                    <select data-toggle="select" name="musicbeat" id="musicbeat"
                            class="form-control select select-success mrs mbm">
                        <option value="四拍">四拍</option>
                        <option value="八拍">八拍</option>
                        <option value="十六拍">十六拍</option>
                        <option value="三十二拍">三十二拍</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="musicstyle">舞蹈风格---
                        <div style="color: green; display: inline; font-size: 18pt">原本为${style}</div>
                    </label>
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
                    <label for="musicletter">伴奏首字母(大小写均可)</label>
                    <input type="text" class="form-control" name="musicletter" id="musicletter" value="${letter}">
                </div>

                <button type="button" class="btn btn-default" id="update">更新舞蹈伴奏信息</button>
                <button type="button" class="btn btn-default" id="kickout">删除舞蹈伴奏</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../footer.jsp" %>
