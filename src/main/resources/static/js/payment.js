document.addEventListener('DOMContentLoaded', function () {
    const container = document.getElementById('paymentContainer') || {};
    const payUrl = container.dataset ? container.dataset.payUrl : '';
    const closeUrl = container.dataset ? container.dataset.closeUrl : '';

    const payBtn = document.getElementById('payBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const resetBtn = document.getElementById('resetBtn');
    const paymentInfoForm = document.getElementById('paymentInfoForm');
    const actionMessage = document.getElementById('actionMessage');
    const agreeSim = document.getElementById('agreeSim');
    const payFallbackForm = document.getElementById('payFallbackForm');
    const closeFallbackForm = document.getElementById('closeFallbackForm');
    const paymentStatusEl = document.getElementById('paymentStatus');

    function showMessage(text, kind) {
        if (!actionMessage) return;
        actionMessage.style.display = 'block';
        actionMessage.className = 'message';
        if (kind === 'success') actionMessage.classList.add('alert-success');
        else if (kind === 'error') actionMessage.classList.add('alert-error');
        actionMessage.textContent = text;
    }

    function clearMessage() {
        if (!actionMessage) return;
        actionMessage.style.display = 'none';
        actionMessage.textContent = '';
        actionMessage.className = 'message';
    }

    function formIsValid() {
        if (!paymentInfoForm) return false;
        return paymentInfoForm.checkValidity() && agreeSim && agreeSim.checked;
    }

    function updatePayBtnState() {
        if (!payBtn) return;
        payBtn.disabled = !formIsValid();
    }

    (function hideFormIfFinal() {
        const status = paymentStatusEl ? String(paymentStatusEl.textContent || '').trim().toUpperCase() : '';
        if (status === 'PAID' || status === 'CLOSED') {
            if (paymentInfoForm && paymentInfoForm.parentNode) {
                paymentInfoForm.parentNode.removeChild(paymentInfoForm);
            }
            if (payBtn) payBtn.style.display = 'none';
            if (cancelBtn) cancelBtn.style.display = 'none';
            if (resetBtn) resetBtn.style.display = 'none';
            clearMessage();
        }
    })();

    if (paymentInfoForm) {
        paymentInfoForm.addEventListener('input', updatePayBtnState);
    }
    if (agreeSim) {
        agreeSim.addEventListener('change', updatePayBtnState);
    }

    if (resetBtn) {
        resetBtn.addEventListener('click', function () {
            if (paymentInfoForm) paymentInfoForm.reset();
            updatePayBtnState();
            clearMessage();
        });
    }

    async function postAction(url, body) {
        if (!url) throw new Error('No endpoint URL provided');

        const headers = new Headers();
        headers.append('Content-Type', 'application/json');

        return await fetch(url, {
            method: 'PUT',
            headers: headers,
            body: JSON.stringify(body || {})
        });
    }

    if (payBtn) {
        payBtn.addEventListener('click', async function () {
            if (!formIsValid()) {
                showMessage('Please fill required fields and agree to the simulation checkbox.', 'error');
                return;
            }

            payBtn.disabled = true;
            if (cancelBtn) cancelBtn.disabled = true;
            showMessage('Processing simulated payment...', 'info');

            try {
                const resp = await postAction(payUrl, {simulated: true});
                if (resp.ok) {
                    if (paymentStatusEl) paymentStatusEl.textContent = 'PAID';
                    showMessage('Payment marked as PAID (simulation).', 'success');
                    if (paymentInfoForm && paymentInfoForm.parentNode) paymentInfoForm.parentNode.removeChild(paymentInfoForm);
                } else {
                    if (payFallbackForm) {
                        payFallbackForm.submit();
                        return;
                    }
                    const txt = await resp.text();
                    showMessage('Server returned error: ' + resp.status + ' — ' + txt, 'error');
                    payBtn.disabled = false;
                    if (cancelBtn) cancelBtn.disabled = false;
                }
            } catch (e) {
                if (payFallbackForm) {
                    payFallbackForm.submit();
                    return;
                }
                showMessage('Network error while sending simulated payment: ' + e, 'error');
                payBtn.disabled = false;
                if (cancelBtn) cancelBtn.disabled = false;
            }
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener('click', async function () {
            if (!confirm('Are you sure you want to cancel this payment? This will set the payment status to CLOSED.')) {
                return;
            }

            if (payBtn) payBtn.disabled = true;
            cancelBtn.disabled = true;
            showMessage('Closing payment...', 'info');

            try {
                const resp = await postAction(closeUrl, {simulated: true});
                if (resp.ok) {
                    if (paymentStatusEl) paymentStatusEl.textContent = 'CLOSED';
                    showMessage('Payment marked as CLOSED (simulation).', 'success');
                    if (paymentInfoForm && paymentInfoForm.parentNode) paymentInfoForm.parentNode.removeChild(paymentInfoForm);
                } else {
                    if (closeFallbackForm) {
                        closeFallbackForm.submit();
                        return;
                    }
                    const txt = await resp.text();
                    showMessage('Server returned error: ' + resp.status + ' — ' + txt, 'error');
                    if (payBtn) payBtn.disabled = false;
                    cancelBtn.disabled = false;
                }
            } catch (e) {
                if (closeFallbackForm) {
                    closeFallbackForm.submit();
                    return;
                }
                showMessage('Network error while closing payment: ' + e, 'error');
                if (payBtn) payBtn.disabled = false;
                cancelBtn.disabled = false;
            }
        });
    }

    updatePayBtnState();
});