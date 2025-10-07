package com.psjw.product.appliaction;

import com.psjw.product.appliaction.dto.ProductReserveCommand;
import com.psjw.product.appliaction.dto.ProductReserveConfirmCommand;
import com.psjw.product.appliaction.dto.ProductReserveResult;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductFacadeService {

    private final ProductService productService;

    public ProductFacadeService(ProductService productService) {
        this.productService = productService;
    }



    public ProductReserveResult tryReserve(ProductReserveCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try{
                return productService.tryReserve(command);

            }catch (ObjectOptimisticLockingFailureException e){
                tryCount++;
            }
        }

        throw new RuntimeException("예약에 실패하였습니다.");
    }


    @Transactional
    public void confirmReserve(ProductReserveConfirmCommand command) {
        int tryCount = 0;

        while (tryCount < 3) {
            try{
                productService.confirmReserve(command);
                return;

            }catch (ObjectOptimisticLockingFailureException e){
                tryCount++;
            }
        }

        throw new RuntimeException("예약에 실패하였습니다.");
    }
}

