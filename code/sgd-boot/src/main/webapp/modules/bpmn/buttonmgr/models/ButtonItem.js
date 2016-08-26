/**
 * Created by DELL on 2016/5/17.
 */
define(function () {
    return fish.Model.extend({
        idAttribute: 'BUTTON_ID',

        defaults: {
            "BTN_ID": null,
            "BTN_NAME": null,
            "PAGE_URL": "",
            "COMMENTS": ""
        },

        initialize: function() {
          
        }
    });
});