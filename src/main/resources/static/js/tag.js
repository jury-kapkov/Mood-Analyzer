$(document).ready(function(){
    $(".tag-delete-form").ajaxForm({
        url: $(this).attr("action"),
        method: "DELETE",
        success: function (response) {
            window.location.pathname = "/admin/tags";
        }
    });
     $(".mark-delete-form").ajaxForm({
        url: $(this).attr("action"),
        method: "DELETE",
        success: function (response) {
            window.location.pathname = "/admin/marks";
        }
    });
    $("#tag-add-form").ajaxForm({
        url: $(this).attr("action"),
        method: "POST",
        success: function (response) {
            window.location.pathname = "/admin/tags";
        }
    });
      $("#mark-add-form").ajaxForm({
            url: $(this).attr("action"),
            method: "POST",
            success: function (response) {
                window.location.pathname = "/admin/marks";
            }
        });
});