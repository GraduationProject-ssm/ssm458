package com.entity.view;

import com.entity.ShiyantaishiyongEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 实验台使用记录
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 */
@TableName("shiyantaishiyong")
public class ShiyantaishiyongView extends ShiyantaishiyongEntity implements Serializable {
    private static final long serialVersionUID = 1L;




		//级联表 shiyantai
			/**
			* 实验台编号
			*/
			private String shiyantaiBianhao;
			/**
			* 实验台名称
			*/
			private String shiyantaiName;
			/**
			* 所在位置
			*/
			private String shiyantaiWeizhi;
			/**
			* 备注
			*/
			private String shiyantaiContent;

		//级联表 xuesheng
			/**
			* 学号
			*/
			private String xueshengUuidNumber;
			/**
			* 学生姓名
			*/
			private String xueshengName;
			/**
			* 学生手机号
			*/
			private String xueshengPhone;
			/**
			* 学生头像
			*/
			private String xueshengPhoto;
			/**
			* 班级
			*/
			private Integer banjiTypes;
				/**
				* 班级的值
				*/
				private String banjiValue;
			/**
			* 学生介绍
			*/
			private String xueshengContent;
			/**
			* 电子邮箱
			*/
			private String xueshengEmail;

	public ShiyantaishiyongView() {

	}

	public ShiyantaishiyongView(ShiyantaishiyongEntity shiyantaishiyongEntity) {
		try {
			BeanUtils.copyProperties(this, shiyantaishiyongEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}































				//级联表的get和set shiyantai

					/**
					* 获取： 实验台编号
					*/
					public String getShiyantaiBianhao() {
						return shiyantaiBianhao;
					}
					/**
					* 设置： 实验台编号
					*/
					public void setShiyantaiBianhao(String shiyantaiBianhao) {
						this.shiyantaiBianhao = shiyantaiBianhao;
					}
					/**
					* 获取： 实验台名称
					*/
					public String getShiyantaiName() {
						return shiyantaiName;
					}
					/**
					* 设置： 实验台名称
					*/
					public void setShiyantaiName(String shiyantaiName) {
						this.shiyantaiName = shiyantaiName;
					}
					/**
					* 获取： 所在位置
					*/
					public String getShiyantaiWeizhi() {
						return shiyantaiWeizhi;
					}
					/**
					* 设置： 所在位置
					*/
					public void setShiyantaiWeizhi(String shiyantaiWeizhi) {
						this.shiyantaiWeizhi = shiyantaiWeizhi;
					}
					/**
					* 获取： 备注
					*/
					public String getShiyantaiContent() {
						return shiyantaiContent;
					}
					/**
					* 设置： 备注
					*/
					public void setShiyantaiContent(String shiyantaiContent) {
						this.shiyantaiContent = shiyantaiContent;
					}







				//级联表的get和set xuesheng
					/**
					* 获取： 学号
					*/
					public String getXueshengUuidNumber() {
						return xueshengUuidNumber;
					}
					/**
					* 设置： 学号
					*/
					public void setXueshengUuidNumber(String xueshengUuidNumber) {
						this.xueshengUuidNumber = xueshengUuidNumber;
					}
					/**
					* 获取： 学生姓名
					*/
					public String getXueshengName() {
						return xueshengName;
					}
					/**
					* 设置： 学生姓名
					*/
					public void setXueshengName(String xueshengName) {
						this.xueshengName = xueshengName;
					}
					/**
					* 获取： 学生手机号
					*/
					public String getXueshengPhone() {
						return xueshengPhone;
					}
					/**
					* 设置： 学生手机号
					*/
					public void setXueshengPhone(String xueshengPhone) {
						this.xueshengPhone = xueshengPhone;
					}
					/**
					* 获取： 学生头像
					*/
					public String getXueshengPhoto() {
						return xueshengPhoto;
					}
					/**
					* 设置： 学生头像
					*/
					public void setXueshengPhoto(String xueshengPhoto) {
						this.xueshengPhoto = xueshengPhoto;
					}
					/**
					* 获取： 班级
					*/
					public Integer getBanjiTypes() {
						return banjiTypes;
					}
					/**
					* 设置： 班级
					*/
					public void setBanjiTypes(Integer banjiTypes) {
						this.banjiTypes = banjiTypes;
					}


						/**
						* 获取： 班级的值
						*/
						public String getBanjiValue() {
							return banjiValue;
						}
						/**
						* 设置： 班级的值
						*/
						public void setBanjiValue(String banjiValue) {
							this.banjiValue = banjiValue;
						}
					/**
					* 获取： 学生介绍
					*/
					public String getXueshengContent() {
						return xueshengContent;
					}
					/**
					* 设置： 学生介绍
					*/
					public void setXueshengContent(String xueshengContent) {
						this.xueshengContent = xueshengContent;
					}
					/**
					* 获取： 电子邮箱
					*/
					public String getXueshengEmail() {
						return xueshengEmail;
					}
					/**
					* 设置： 电子邮箱
					*/
					public void setXueshengEmail(String xueshengEmail) {
						this.xueshengEmail = xueshengEmail;
					}






}
