/**
 * Created by DELL on 2016/5/11.
 */
define([
    "text!modules/commonbiz/templates/DictionaryMgrTemplate.html",
    'DictionTypeMgr',
    'modules/commonbiz/views/DictionDataMgr'
], function(DictionaryMgrTpl, DictionTypeMgr, DictionDataMgr) {
    return portal.BaseView.extend({
        tagName: "div",
        template: fish.compile(DictionaryMgrTpl),
        initialize: function() {
            this.setViews({
                ".col-lg-4": new DictionTypeMgr(),
                ".col-lg-8": new DictionDataMgr()
            });
        },

        render: function() {
            this.$el.html(this.template());

            this.getView(".col-lg-4").on("orgChange", this.orgChange, this);
        },
        orgChange: function(portal) {
            this.getView(".col-lg-8").trigger("orgChange", portal);
        },

        afterRender: function() {

        },
        resize: function(delta){
            portal.utils.gridIncHeight(this.$(".js-dictionaryType-grid"), delta);
            portal.utils.gridIncHeight(this.$(".js-dictionaryData-grid"), this.$(".js-left-div").height() - this.$(".js-right-div").height() + 2);

        }
    })
});