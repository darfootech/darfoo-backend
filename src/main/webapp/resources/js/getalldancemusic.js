$(function () {
    $.ajax({
        url: '/darfoobackend/rest/resources/dancemusic/all/service',
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