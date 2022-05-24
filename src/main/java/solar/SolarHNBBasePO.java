package solar;

import lombok.Data;
/**
 * 天合光伏惠农宝基本信息Bean
 *
 * @author Sxuet
 * @since 2022.05.22 22:36
 */
@Data
public class SolarHNBBasePO {

  private Integer id;

  private String loanNo;

  private String applyTime;

  private Integer applyStatus;

  private String eleCardNo;

  private String name;

  private String idCard;

  private String idCardAddr;

  private String tel;

  private String prospectNo;

  private String loanGoodsName;

  private String contactOption;

  private String nameOption;

  private String relationshipOption;

  private String cardNo;

  private String bankName;

  private String addrEx;

  private String addr;

  private Integer ecuNum;

  private Integer capacity;

  private String twoWayNo;

  private String houseSitePic;

  private String houseHoldPic;

  private String pictureUrl;

  private Object receiptFile;

  private Boolean isInstall;

  private String idCardFront;

  private String idCardBack;

  private Object installPicUrl;
}
