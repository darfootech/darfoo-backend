$(function () {
    var type = $("#type").text();

    $("#start").click(function () {
        $.ajax({
            type: "POST",
            url: "/darfoobackend/rest/resources/" + type + "/create",
            data: $("#createresourceform").serialize(),
            success: function (data) {
                if (data == "200") {
                    alert("提交资源信息成功");
                    window.location.href = "/darfoobackend/rest/resources/" + type + "resource/new"
                } else if (data == "500") {
                    alert("资源名字与明星舞队的组合已经存在,请更换资源标题或者名字");
                } else if (data == "501") {
                    alert("相同名字的图片已经存在了,请修改图片名字");
                } else if (data == "502") {
                    alert("明星舞队还不存在");
                } else if (data == "503") {
                    alert("请保证资源首字母是单个英文字符");
                } else if (data == "504") {
                    alert("请保证图片名字带有后缀");
                } else if (data == "505") {
                    alert("伴奏名字和作者名字组合已经存在,请修改伴奏名字");
                } else if (data == "506") {
                    alert("相同名字的明星舞队已经存在");
                } else {
                    alert("提交视频信息失败");
                }
            },
            error: function () {
                alert("提交资源信息失败");
            }
        });
    });

    $.ajax({
        url: '/darfoobackend/rest/resources/music/all/service',
        type: 'GET',
        data: {},
        success: function (response) {
            console.log(response);

            var states = new Bloodhound({
                datumTokenizer: function (d) {
                    return Bloodhound.tokenizers.whitespace(d.word);
                },
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