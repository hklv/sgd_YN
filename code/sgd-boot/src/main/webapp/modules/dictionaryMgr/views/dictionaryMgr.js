define([
    "text!modules/dictionaryMgr/templates/dictionaryTemplate.html",
    "modules/dictionaryMgr/views/dictionaryTypeViewMgr",
    "modules/dictionaryMgr/views/dictionaryDataViewMgr"
], function(template, dictionaryTypeMgrView, dictionaryDataMgrView) {
    return portal.BaseView.extend({
        template: fish.compile(template),
        initialize: function() {
            this.setViews({
                ".js-left-div": new dictionaryTypeMgrView(),
                ".js-right-div": new dictionaryDataMgrView()
            });
        },
        render: function() {
            this.$el.html(this.template());
            this.getView(".js-left-div").on("dictTypeChange", this.dictTypeChange, this);
        },
        dictTypeChange: function(data) {
            this.getView(".js-right-div").trigger("dictTypeChange", data);
        }
        resize: function(delta) {
            portal.utils.gridIncHeight(this.$(".js-type-grid"), delta);
            portal.utils.gridIncHeight(this.$(".js-data-grid"), this.$(".js-left-div").height() - this.$(".js-right-div").height() + 2);
        }

    });
});
