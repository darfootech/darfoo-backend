/**
 * Created by zjh on 15-2-26.
 */

$(function () {
    var type = $("type").text();

    $("#update").click(function() {
        var createauthorUrl = "/darfoobackend/rest/admin/author/update";

        $.ajax({
            type: "POST",
            url: createauthorUrl,
            data: $("#createauthorform").serialize(),
            success: function (data) {
                if (data == "200") {
                    alert("更新作者信息成功");
                    window.location.href = "/darfoobackend/rest/admin/author/all"
                } else if (data == "201") {
                    alert("更新作者信息成功");
                    window.location.href = "/darfoobackend/rest/resources/authorresource/new"
                } else if (data == "501") {
                    alert("相同名字的作者已经存在了，请修改作者名字")
                } else if (data == "505") {
                    alert("相同名字的图片已经存在了，请修改图片名字");
                } else if (data == "508") {
                    alert("请填写并上传作者相关的图片");
                } else {
                    alert("更新作者信息失败");
                }
            },
            error: function () {
                alert("更新作者信息失败");
            }
        });
    });

    $("#kickout").click(function(){
        var authorid = $("#authorid").text();

        var targeturl = "/darfoobackend/rest/admin/author/delete";

        $.ajax({
            type: "POST",
            url: targeturl,
            data: {"id": authorid},
            success: function (data) {
                if (data == "200") {
                    alert("删除作者信息成功");
                    window.location.href = "/darfoobackend/rest/admin/author/all"
                } else if (data == "505") {
                    alert("删除作者信息失败");
                } else {
                    alert("删除作者信息失败");
                }
            },
            error: function () {
                alert("删除作者信息失败");
            }
        });
    });

    $("#updateimage").click(function(){
        window.location.href = "/darfoobackend/rest/admin/author/updateimage/" + $("#authorid").text();
    });
});