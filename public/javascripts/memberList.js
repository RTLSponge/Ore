/*
 * ==================================================
 *  _____             _
 * |     |___ ___    |_|___
 * |  |  |  _| -_|_  | |_ -|
 * |_____|_| |___|_|_| |___|
 *                 |___|
 *
 * By Walker Crouse (windy) and contributors
 * (C) SpongePowered 2016-2017 MIT License
 * https://github.com/SpongePowered/Ore
 *
 * Powers the member list seen on the project creation page, project settings
 * and organization page.
 *
 * ==================================================
 */

/*
 * ==================================================
 * =                Helper functions                =
 * ==================================================
 */

function updateIndices() {
    var memberList = $('.list-members');
    memberList.find('.user-new').each(function(i) {
        $(this).find('input').attr('name', 'users[' + i + ']');
        $(this).find('select').attr('name', 'roles[' + i + ']');
    });

    memberList.find('.user-changed').each(function(i) {
        $(this).find('input').attr('name', 'userUps[' + i + ']');
        $(this).find('select').attr('name', 'roleUps[' + i + ']');
    });
}

function getItemContainer(element) {
    return element.closest('.list-group-item');
}

function initMember(memberRow) {
    // Replace title with select on click
    memberRow.find('.fa-edit').parent().click(function(event) {
        event.preventDefault();

        var saveBtn = $('.btn-members-save');
        if (!saveBtn.is(':visible'))
            saveBtn.fadeIn('fast');

        // Mark user as changed
        var container = getItemContainer($(this)).addClass('user-changed');
        var input = $('#select-role').clone().removeAttr('id').attr('form', 'save');

        // Add input
        container.find('span').replaceWith(input.show());
        var username = container.find('.username').text();
        container.append('<input type="hidden" form="save" value="' + username + '" />');

        // Remove edit button and update input names
        $(this).find('.fa-edit').parent().remove();
        updateIndices();
    });

    // Set form input on modal when delete is clicked
    memberRow.find('.fa-trash').parent().click(function(event) {
        event.preventDefault();
        $('#modal-user-delete').find('input').val(getItemContainer($(this)).find('.username').text());
    });
}

/*
 * ==================================================
 * =                   Doc ready                    =
 * ==================================================
 */

$(function() {
    initMember($('.list-members').find('.list-group-item'));

    initUserSearch(function(result) {
        var alert = $('.alert-danger');
        var message = alert.find('span');
        if (!result.isSuccess) {
            message.text('Could not find user with name "' + result.username + '".');
            alert.fadeIn();
            return;
        }
        alert.fadeOut();

        var saveBtn = $('.btn-members-save');
        if (!saveBtn.is(':visible'))
            saveBtn.fadeIn('fast');

        var user = result.user;
        // Check if user is already defined
        if ($('.list-members').find('a[href="/' + user.username + '"]').length) {
            return;
        }

        // Build result row
        var resultRow = $('#row-user').clone().removeAttr('id').addClass('user-new');
        resultRow.find('a').attr('href', '/' + user.username).text(user.username);
        resultRow.find('input').attr('form', 'save').val(user.id);
        resultRow.find('select').attr('form', 'save');
        resultRow.find('i').click(function() {
            $(this).parent().remove();
            updateIndices();
        });


        var avatarImg = resultRow.find('img');
        if (user.hasOwnProperty('avatarTemplate')) {
            var avatarUrl = user.avatarTemplate.replace('{size}', '100');
            avatarImg.attr('src', avatarUrl);
        } else {
            avatarImg.remove();
        }

        // Add result to list
        $('.user-search').parent().before(resultRow);
        updateIndices();
    });

    updateIndices();
});
