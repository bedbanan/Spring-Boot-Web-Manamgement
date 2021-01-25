require(
    ['/jscustom/GlobleConfig.js'],
    function() {
        requirejs(
            ['jquery', 'bootstrap', 'custom', 'bootstrap_table', 'bootstrap_table_CN', 'layer'],
            function ($) {
                $("#btn_add").on('click',function(){
                    layer.open({
                        type: 2,
                        skin: 'layui-layer-molv',
                        title: 'cron表达器',
                        shadeClose: true,
                        shade: 0.4,
                        maxmin: false,
                        area: ['900px', '800px'],
                        content: '/job/crone',
                        end: function () {
                            $("#tb_Company").bootstrapTable('refresh');
                        }
                    });
                });
            }
        )
    }
)
