/**
 * Created by zjh on 15-2-26.
 */

$(function () {
    var type = $("#type").text();
    var id = $("#id").text();

    $("#update").click(function () {
        $.ajax({
            type: "POST",
            url: "/darfoobackend/rest/admin/" + type + "/update",
            data: $("#updateresourceform").serialize(),
            success: function (data) {
                if (data == "200") {
                    alert("更新资源信息成功");
                    window.location.href = "/darfoobackend/rest/admin/gallery/" + type + "/all"
                } else if (data == "500") {
                    alert("需要被更新的资源不存在");
                } else if (data == "501") {
                    alert("要更新的资源的关联的明星舞队不存在")
                } else if (data == "502") {
                    alert("资源名字和明星舞队组合已存在");
                } else if (data == "503") {
                    alert("请保证资源首字母是单个英文字符");
                } else if (data == "504") {
                    alert("相同名称的明星舞队已经存在");
                } else if (data == "505") {
                    alert("伴奏名字和舞队名字组合已存在");
                } else if (data == "506") {
                    alert("请保证图片名字带有后缀");
                } else if (data == "507") {
                    alert("相同名字的图片已经存在了,请修改图片名字");
                } else {
                    alert("更新资源信息失败");
                }
            },
            error: function () {
                alert("更新舞队信息失败");
            }
        });
    });

    $("#kickout").click(function () {
        $.ajax({
            type: "POST",
            url: "/darfoobackend/rest/admin/" + type + "/delete",
            data: {"id": id},
            success: function (data) {
                if (data == "200") {
                    alert("删除资源信息成功");
                    window.location.href = "/darfoobackend/rest/admin/gallery/" + type + "/all"
                } else if (data == "500") {
                    alert("删除资源信息失败");
                } else {
                    alert("删除资源信息失败");
                }
            },
            error: function () {
                alert("删除资源信息失败");
            }
        });
    });

    $("#updateimage").click(function () {
        window.location.href = "/darfoobackend/rest/admin/" + type + "/updateimage/" + id;
    });

    $("#updatevideo").click(function () {
        window.location.href = "/darfoobackend/rest/admin/" + type + "/updatevideo/" + id;
    });

    $("#updatemusic").click(function () {
        window.location.href = "/darfoobackend/rest/admin/" + type + "/updatemusic/" + id;
    });
});