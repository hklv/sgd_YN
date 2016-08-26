module.exports = function (grunt) {
    require('load-grunt-tasks')(grunt);
    grunt.file.defaultEncoding = 'utf8';
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        requirejs: {
          compile: {
            options: {

              optimize: 'none',
              baseUrl: "./code/portal/src/main/webapp",
              // mainConfigFile: "../dest/app/config.js",
              //name: 'main',
              out: "./code/portal/src/main/webapp/iframe/portal-all.js",
              preserveLicenseComments: false,
              findNestedDependencies: true,
              removeCombined: true,
              //stubModules: ['text', 'undomanager'],
              // paths: {
              //   'text': 'grunt/fish.text',
              //   'i18n':'grunt/fish.i18n'
              // },
              // dir: '../dest/build',
              // wrap: {
              //   endFile: "./grunt/requirejs.end.js"
              // },
              // modules: [{
              //   name: 'build',
              //   create: true,
                include: [
                  'frm/portal/Portal',
                  'i18n/common.en',
                  'i18n/common.zh'
                ]//,
                // exclude: []
              //}],
              // fileExclusionRegExp: /(asset|docs|node_modules|test)/
            }
          }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-requirejs');
   
    grunt.registerTask('default', ['requirejs']);
};
