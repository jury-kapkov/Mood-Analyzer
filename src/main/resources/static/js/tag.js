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

 function edit(element) {
        var button = document.getElementById("action_button");
        var id = element.id;
        var data = element.dataset.text;
        if (button.textContent == "Добавить"){
                var temp = document.getElementById("id_for_editing");
                temp.value = id;
                var edit_input = document.getElementById("data_for_editing");
                edit_input.value = data;

                var edit_form = document.getElementById("tag-edit-form");
                var add_form = document.getElementById("tag-add-form");
                edit_form.classList.remove("display_none");
                edit_form.classList.add("display_flex");

                add_form.classList.remove("display_flex");
                add_form.classList.add("display_none");

                var button_edit = document.getElementById("action_button_edit");
                button_edit.classList.add("add_button");
        } else {
            var temp = document.getElementById("id_for_editing");
            temp.value = id;
            var edit_input = document.getElementById("data_for_editing");
            edit_input.value = data;
        }
    }