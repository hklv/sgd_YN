define(function() {
	return {
		qryAllJobs: function(state, success) {
			portal.callService("QryAllJobs", {
				'JOB_NAME': null,
				'STATE': state
			}, success);
		},
		qryRoles: function(jobId, state, success) {
			portal.callService("QryRolesByJobId", {
				JOB_ID: jobId,
				'STATE': state
			}, success);
		},
		qryAllRoles: function(success, ctx) {
			portal.callService("QryRoleList", {}, success);
		},
		addRoles2Job: function(jobId, roleIdList, success) {
			portal.callService("AddRolesToJob", {
				JOB_ID: jobId,
				ROLE_ID_LIST: roleIdList
			}, success);
		},
		addRoles2JobAndUsers: function(jobId, roleIdList, userIdList, success) {
			portal.callService("AddRolesToJobAndUsers", {
				JOB_ID: jobId,
				ROLE_ID_LIST: roleIdList,
				USER_ID_LIST: userIdList
			}, success);
		},
		delRoleFromJob: function(jobId, roleId, success) {
			portal.callService("DelRolesFromJob", {
				JOB_ID: jobId,
				ROLE_ID_LIST: [roleId]
			}, success);
		},
		delRoleFromJobAndUsers: function(jobId, roleId, userIdList, success) {
			portal.callService("DelRolesFormJobAndUsers", {
				JOB_ID: jobId,
				ROLE_ID_LIST: [roleId],
				USER_ID_LIST: userIdList
			}, success);
		},
		addJob: function(jobName, state, success) {
			portal.callService("AddJob", {
				JOB_NAME: jobName,
				STATE: state
			}, success);
		},
		modJob: function(jobId, jobName, success) {
			portal.callService("ModJob", {
				JOB_ID: jobId,
				JOB_NAME: jobName
			}, success);
		},
		delJob: function(jobId, success) {
			portal.callService("DelJob", {
				JOB_ID: jobId
			}, success);
		},
		disableJob: function(jobId, success) {
			portal.callService("DisableJob", {
				JOB_ID: jobId
			}, success);
		},
		enableJob: function(jobId, success) {
			portal.callService("EnableJob", {
				JOB_ID: jobId
			}, success);
		},
		qryUserInfoByJobId: function(jobId, success) {
			portal.callService("QryOrgAreaStaffUserInfoByJobId", {
				JOB_ID: jobId,
				STATE: 'A'
			}, success)
		}
	}
});