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
     $("#mark-add-form").ajaxForm({
        url: $(this).attr("action"),
        method: "POST",
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

     $("#tag-edit-form").ajaxForm({
            url: $(this).attr("action"),
            method: "PATCH",
            success: function (response) {
                window.location.pathname = "/admin/tags";
            }
        });
});