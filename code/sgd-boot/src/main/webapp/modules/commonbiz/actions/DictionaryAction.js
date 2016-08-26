define(function() {
    return {
        qryDictionaryTypeList: function() {

            var data  = [{
                "id" : 1,
                "name" : "wu",
                "code" : "123eee",
                "state": "1",
                //"createUser": "w",
                //"updateUser": "ff",
                //"update_date": "2014",
                "comments": "sssssss"
            }];
            return data;
        },
        qryPortalList: function(success) {
            portal.callService("QryPortalList", {}, success);
        },
        qryUserListCount: function(qryConditionJson, success) {
            portal.callService("QryUserList", qryConditionJson, success, {
                Count: true
            });
        },
        qryUserListByPageInfo: function(qryConditionJson, filter, success) {
            portal.callService("QryUserList", qryConditionJson, success, filter);
        },

        addUser: function(userDetailJSON, success) {
            portal.callService("AddUser", userDetailJSON, success);
        },
        modUser: function(userDetailJSON, success) {
            portal.callService("ModUser", userDetailJSON, success);
        },
        qryRoleList: function(success) {
            portal.callService("QryRoleList", {}, success);
        },
        qryRoleListByUserId: function(userId, success) {
            portal.callService("QryRoleListByUserId", {
                'USER_ID': userId
            }, success);
        },
        grantRole2User: function(userId, roleList, success) {
            portal.callService("GrantRoleToUser", {
                USER_ID: userId,
                ROLE_LIST: roleList
            }, success);
        },
        qryPortalList: function(success) {
            portal.callService("QryPortalList", {}, success);
        },
        qryUserPortalListByUserId: function(userId, success) {
            portal.callService('QryUserPortalListByUserId', {
                'USER_ID': userId
            }, success);
        },
        qryRolePortalListByUserId: function(userId, success) {
            portal.callService('QryRolePortalListByUserId', {
                'USER_ID': userId
            }, success);
        },
        grantPortal2User: function(userId, portalId, portalList, success) {
            portal.callService("GrantPortalToUser", {
                USER_ID: userId,
                PORTAL_ID: portalId,
                PORTAL_LIST: portalList
            }, success);
        },
        qryMenuListByUserId: function(userId, success) {
            portal.callService("QryPrivListByUserId", {
                USER_ID: userId,
                PRIV_TYPE: "M"
            }, success);
        },
        qryPortalListByUserId: function(userId, success) {
            portal.callService("QryPortalListByUserId", {
                USER_ID: userId
            }, success);
        },
        qrySubDirListByParentInPortal: function(options, success) {
            portal.callService("QrySubDirListByParentInPortal", fish.extend({
                STATE: 'A'
            }, options), success);
        },
        qryDirListByParentId: function(dirId, success) {
            portal.callService("QryDirListByParentId", {
                DIR_ID: dirId,
                STATE: 'A'
            }, success);
        },
        qryDirMenuList: function(dirId, success) {
            portal.callService("QryDirMenuListByParentId", {
                'DIR_ID': dirId
            }, success);
        },
        qryMenuListInPortalByDirIdLeftJoinUserOwnedInfo: function(options, success) {
            portal.callService("QryMenuListInPortalByDirIdLeftJoinUserOwnedInfo", fish.extend({
                STATE: 'A'
            }, options), success);
        },
        qryMenuListByDirIdLeftJoinUserOwnedInfo: function(options, success) {
            if (options.DIR_ID !== -1) {
                portal.callService("QryMenuListByDirIdLeftJoinUserOwnedInfo", fish.extend({
                    STATE: 'A'
                }, options), success);
            } else {
                success([]);
            }
        },
        grantPriv2User: function(userId, privLevel, privList, success) {
            portal.callService("AddPrivsToUserPriv", {
                USER_ID: userId,
                PRIV_LEVEL: privLevel,
                PRIV_LIST: privList
            }, success);
        },
        degrantPrivFromUser: function(userId, privList, success) {
            portal.callService("DelPrivsFromUserPriv", {
                USER_ID: userId,
                PRIV_LIST: privList
            }, success);
        },
        qryCompListByUserId: function(userId, success) {
            portal.callService("QryUserComponentPrivList", {
                USER_ID: userId
            }, success);
        },
        qrySubDirMenuListByParentInPortal: function(options, success) {
            portal.callService("QrySubDirMenuListByParentInPortal", fish.extend({
                STATE: 'A'
            }, options), success);
        },
        qryCompListInMenuAndUserPrivLevel: function(options, success) {
            portal.callService("QryCompListInMenyAndUserPrivLevel", fish.extend({
                STATE: 'A'
            }, options), success);
        },
        disableUser: function(userId, success) {
            portal.callService("DisableUser", {
                USER_ID: userId
            }, success);
        },
        enableUser: function(userId, success) {
            portal.callService("EnableUser", {
                USER_ID: userId
            }, success);
        },
        lockUser: function(userId, success) {
            portal.callService("LockUser", {
                USER_ID: userId
            }, success);
        },
        unlockUser: function(userId, success) {
            portal.callService("UnLockUser", {
                USER_ID: userId
            }, success);
        },
        resetPasswd: function(userId, success) {
            portal.callService("ResetUserPwd", {
                USER_ID: userId
            }, success);
        },
        qryDataPrivList: function(success) {
            portal.callService("QryDataPrivList", {}, success);
        },
        qryDataPrivListByUserId: function(userId, success) {
            portal.callService("QryUserDataPrivListByUserId", {
                USER_ID: userId
            }, success);
        },
        grantData2User: function(options, success) {
            portal.callService("AddDataPrivToUser", options, success);
        },
        degrantDataFromUser: function(dataPrivId, userId, success) {
            portal.callService("DelDataPrivFromUser", {
                DATA_PRIV_ID: dataPrivId,
                USER_ID: userId
            }, success);
        },
        addDataPrivToUser: function(data, success) {
            portal.callService("AddDataPrivToUser", data, success);
        },
        qryDataPrivValueListByDataPrivId: function(dataPrivId, success) {
            portal.callService("QryDataPrivValueListByDataPrivId", {
                DATA_PRIV_ID: dataPrivId
            }, success);
        },
        delDataPrivFromUser: function(dataPrivId, userId, success) {
            portal.callService("DelDataPrivFromUser", {
                DATA_PRIV_ID: dataPrivId,
                USER_ID: userId
            }, success);
        },
        editDataPrivOfUser: function(data, success) {
            portal.callService("ModDataPrivOfUser", data, success);
        },
        modUserPrivLevel: function(privId, userId, privLevel, success) {
            portal.callService("ModUserPrivLevel", {
                PRIV_ID: privId,
                USER_ID: userId,
                PRIV_LEVEL: privLevel
            }, success);
        },
        qrySysDate: function(success) {
            portal.callService("QrySysdate", {}, success);
        },

        qryPortletListByPortalIdLeftJoinUserOwnedInfo: function(cond, success) {
            portal.callService("QryPortletListByPortalIdLeftJoinUserOwnedInfo", fish.extend({
                STATE: 'A'
            }, cond), success);
        },
        qryAllPortletType: function(success) {
            portal.callService("QryPortletTypes", {}, success);
        },
        qryPortletListByTypeIdLeftJoinUserOwnedInfo: function(cond, success) {
            portal.callService("QryPortletListByTypeIdLeftJoinUserOwnedInfo", fish.extend({
                STATE: 'A'
            }, cond), success);
        },
        qryUserOwnedPortletList: function(userId, success) {
            portal.callService("QryUserOwnedPortletList", {
                USER_ID: userId,
                STATE: 'A'
            }, success);
        }
    }
});
