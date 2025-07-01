package org.example.Controllers;

import org.example.Models.Invoice;
import org.example.Models.InvoiceDetail;
import org.example.Services.SaleService;

import java.util.List;

public class SalesController {
    private SaleService saleService;

    public SalesController() {
        saleService = new SaleService();
    }

    public boolean checkout(Invoice invoice, List<InvoiceDetail> invoice_details) {
        return saleService.checkout(invoice, invoice_details);
    }
}
