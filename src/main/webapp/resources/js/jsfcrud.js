/*
 * Created by Mayur Dhepe on 2021.10.10
 * Copyright © 2021 Mayur Dhepe. All rights reserved.
 */

function handleSubmit(args, dialog) {
    var jqDialog = jQuery('#' + dialog);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        PF(dialog).hide();
    }
}
