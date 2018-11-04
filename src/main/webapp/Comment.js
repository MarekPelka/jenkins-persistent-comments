function updateComment(button) {
    if (button.innerHTML.toUpperCase().includes('SAVE'))
        saveComment(button.getAttribute('oldComment'));
    else
        editComment()
}

function editComment() {
    var commentElement = document.getElementById('comment');
    var commentText = commentElement.innerHTML;

    changeElementType(commentElement, 'textarea');
    commentElement = document.getElementById('comment');
    //TODO: Change to responsive sizing
    commentElement.style.width = '75vw';
    commentElement.style.height = '200px';
    commentElement.innerHTML = commentText;
    oldComment = commentText;

    var button = document.getElementById('comment-button');
    button.innerHTML = 'Save comment';
    button.setAttribute('oldComment', commentText)
}

function saveComment(oldComment) {
    var commentElement = document.getElementById('comment');
    var newCommentText = commentElement.value;
    //TODO: If comment did not change skip update
    if(oldComment === newCommentText)
        return;
    new Ajax.Request('comment/add?comment=' + newCommentText + '&user=Super current user!', {
        method : 'get',
        onSuccess : function(x) {
            changeElementType(commentElement, 'div');
            commentElement = document.getElementById('comment');
            commentElement.innerHTML = x.responseText;
            document.getElementById('commentDate').innerText = 'Now';
            //TODO: Resize comment div after successful change
            console.log(commentElement);
            console.log(oldComment);
            var button = document.getElementById('comment-button');
            button.innerHTML = 'Edit comment'
        },
        onFailure : function(x) {
            alert(x.statusText);
            document.getElementById('commentDate').innerText = x.statusText
        }
    });
}

function changeElementType(fromElement, targetType) {
    var commentTextarea = document.createElement(targetType),
        old_attributes = fromElement.attributes,
        new_attributes = commentTextarea.attributes;

    for(var i = 0, len = old_attributes.length; i < len; i++) {
        new_attributes.setNamedItem(old_attributes.item(i).cloneNode());
    }

    // This node dose not have any children
// copy child nodes
//     do {
//         commentTextarea.appendChild(fromElement.firstChild);
//     }
//     while(fromElement.firstChild);

    // replace element
    fromElement.parentNode.replaceChild(commentTextarea, fromElement);

}