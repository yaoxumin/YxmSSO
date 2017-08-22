$.extend({
    validator: function(data) {
                /**失去焦点校验开始**/
    	console.log(data);
                $('input, textarea, select, date').on('blur', function(){
                    var $this = $(this);

                    if(data != null && data.hasOwnProperty($this.attr('name'))){
                        var val = '';
                        if($this.is('input[type="radio"]') || $this.is('input[type="checkbox"]')){
                            val = $('input[name="' + $this.attr('name') + '"]:checked').val();
                        } else {
                            val = $this.val();
                        }

                        $.each(data[$this.attr('name')], function(index, obj){
                            if(!obj.hasOwnProperty('trim') || obj.trim == 'true'){
                                val = $.trim(val);
                            };

                            switch(obj.type){

                                case "required":
                                    if(val.length == 0){
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;

                                case "requiredstring":
                                    if(val.length == 0){
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;

                                case "stringlength":
                                    if (val.length < obj.minLength || val.length > obj.maxLength ) {
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;

                                case "int":
                                    if (isNaN(val) || val < obj.min || val > obj.max) {
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;

                                case "date":
                                    if (val < obj.min || val > obj.max){
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;

                                case "regex":
                                    if(!val.match(obj.expression)){
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;

                                case "url":
                                    if(!val.match("^((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?$")){
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;

                                case "email":
                                    if(!val.match("[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?")){
                                        $this.parents('.has-feedback').removeClass('has-success').addClass('has-error');
                                        $this.next('.form-control-feedback').removeClass('fa-check-circle').addClass('fa-times-circle');
                                        layer.msg(obj.message);
                                        break;
                                    } else {
                                        $this.parents('.has-feedback').removeClass('has-error').addClass('has-success');
                                        $this.next('.form-control-feedback').removeClass('fa-times-circle').addClass('fa-check-circle');
                                    }
                                    break;
                            }
                        })
                    }
                });
                /**失去焦点校验结束**/


                /**表单提交校验结束**/
    }
});