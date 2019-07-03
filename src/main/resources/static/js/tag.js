$(document).ready(function(){
    $(".tag-delete-form").ajaxForm({
        url: $(this).attr("action"),
        method: "DELETE",
        success: function (response) {
            window.location.pathname = "/admin/tags";
        },
        error: function (error) {
            if (error.status != 200) {
                $("#error-modal .modal-body p").text(error.responseJSON.message);
                $("#error-modal").modal();
            }
        }
    });
});