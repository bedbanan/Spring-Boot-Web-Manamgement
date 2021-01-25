/*
* 功能：任务列表异步提交
* 作者：啦啦啦
* 时间：2021-01-23 21:04:50
* */
require(
    ['/jscustom/GlobleConfig.js'],
    function () {
        requirejs(
            ['jquery', 'bootstrap3','bootstrap_validator','bootstrap_validator_CN','jqueryform','jqueryupload'],
            function ($) {
                //start 该处定义我们自己的脚本
                $('#form_Job').bootstrapValidator({
                    feedbackIcons: {
                        valid: 'fa fa-check',
                        invalid: 'fa fa-close',
                        validating: 'fa fa-refresh'
                    },
                    fields: {
                        jobName: {
                                notEmpty: {message: '任务名称不能为空'}


                        },
                        jobGroup: {
                                notEmpty: {message: '任务分组不能为空'}

                        },
                        description: {
                                notEmpty: {message: '详细描述不能为空'}

                        },
                        jobClassName:{
                            notEmpty: {message: '执行类不能为空'}
                        },
                        jobMethodName:{
                            notEmpty: {message: '执行方法不能为空'}
                        },
                        cronExpression:{
                            notEmpty: {message: '执行时间不能为空'}
                        }
                    }
                });

                //异步提交表单
                $("#btn_Save").bind('click',function() {
                        var bootstrapValidator = $('#form_Job').data('bootstrapValidator');//获取表单对象
                            var options = {
                                complete: function (data) {

                                    var mylay = parent.layer.getFrameIndex(window.name);
                                    parent.layer.close(mylay);//关闭当前窗口页
                                },
                                url: '/job/add',
                                dataType: 'json',
                                resetForm: true  // 成功提交后，重置所有的表单元素的值
                            };
                            $('#form_Job').ajaxSubmit(options);

                    }
                );

                //end 该处定义我们自己的脚本
            }
        )
    }
);