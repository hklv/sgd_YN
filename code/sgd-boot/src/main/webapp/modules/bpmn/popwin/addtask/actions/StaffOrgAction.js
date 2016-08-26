define(function() {
    return {
        qryStaffMasterOrgList: function(success) {
            portal.callService("QryStaffMasterOrgList", {
                "STATE": "A"
            }, success);
        },
        queryActiveStaffByOrg: function(orgId, success) {
            portal.callService("QryStaffUserListByOrg", {
                ORG_ID: orgId,
                STATE: "A"
            }, success);
        },
    }
})
