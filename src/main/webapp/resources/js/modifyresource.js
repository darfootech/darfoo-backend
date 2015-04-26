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
                } else if (data == "508") {
                    alert("要更新的资源的的越剧连续剧不存在，请先完成越剧连续剧信息的创建");
                } else if (data == "509") {
                    alert("资源名字和越剧连续剧id组合已存在，不可以进行插入了，是否需要修改");
                } else if (data == "510") {
                    alert("相同名字的越剧连续剧已经存在了");
                } else if (data == "511") {
                    alert("已经存在同名的越剧电影,不可以进行插入");
                } else {
                    alert("更新资源信息失败");
                }
            },
            error: function () {
                alert("更新资源信息失败");
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

    $(".updateresource").click(function () {
        var updatetype = $(this).attr("id");
        window.location.href = "/darfoobackend/rest/admin/" + type + "/" + updatetype + "/" + id;
    });
});