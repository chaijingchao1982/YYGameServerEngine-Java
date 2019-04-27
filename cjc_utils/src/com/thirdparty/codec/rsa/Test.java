package com.thirdparty.codec.rsa;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

public class Test {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static final String RSA_PRIVATE = "MIICXgIBAAKBgQDMOCGsuIgBv95ahmyttWywTN/81sSI7BR8Sr5pjbrx3sd75hhu"
			+ "OKlkXsc4LGNY5/VTqPJiiqxHeWA1obqo3WVTo9JutbzDnklYJRKqCFWFLnKupyXE"
			+ "BkmB/3PVNaMVAXs6s+mOUtJW/hzvx8pCSfG0eCjn/zmbpfiBXlzdxIYfMwIDAQAB"
			+ "AoGAAgQf2PutZA8H2+7tzb03yHnJ0E4yfIOQJDX8sUAT82VWpNN9tEDc8z+4Scau"
			+ "aGdJ1byaP/zMfJwnJgBlvlsJMAirZEdRjVd65dhD+KEaegItqPi6ZypprpdqU31C"
			+ "UQO+tLJhQ4/K2eeSme7Rk0ROw2UqqxAuOrGCjfyOfyKxGLECQQD4Hvdxyhq1YQK3"
			+ "IcKirZYY9vA/V/Y0lkR7y4f6mhOrkoFCCGWCMY+5nco0E2ndq7EDBGauH5aCVE6T"
			+ "QTZ1/e2JAkEA0rRHG6gDHEALX8UGoCxwmvTCmrKLa0gC6pCZXSPNemvprtQgFn+p"
			+ "bKezdQvhkdm3hCcvE6gdxioihtx3wGTT2wJBAKqs2neKl4622aBRd8phb8NiceaQ"
			+ "wkpwJ1GksrferrMxxvo+Pl0tbHk5IjNqO0OBa/TwXRVfoy+pgJdon2bQskkCQQCu"
			+ "3o3RZyxFquWFsyhfkOJXyGEmMJ5DCD3bXOzbfxZOzvbfcQ+8fHEpSzlGH/kyqeWD"
			+ "2V1ZzAIB8AA9uwXr+AJXAkEAzmpIm6DIqZly1vFiSmYgYo0FlDtn4qoa3JsstgV5"
			+ "1JYhRT8j9gwb9iDZDQVDdukh5umltL/MCzPGy1ddWfx9jg==";

	public static final String RSA_PRIVATE_PKCS8 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMw4Iay4iAG/3lqG"
			+ "bK21bLBM3/zWxIjsFHxKvmmNuvHex3vmGG44qWRexzgsY1jn9VOo8mKKrEd5YDWh"
			+ "uqjdZVOj0m61vMOeSVglEqoIVYUucq6nJcQGSYH/c9U1oxUBezqz6Y5S0lb+HO/H"
			+ "ykJJ8bR4KOf/OZul+IFeXN3Ehh8zAgMBAAECgYACBB/Y+61kDwfb7u3NvTfIecnQ"
			+ "TjJ8g5AkNfyxQBPzZVak0320QNzzP7hJxq5oZ0nVvJo//Mx8nCcmAGW+WwkwCKtk"
			+ "R1GNV3rl2EP4oRp6Ai2o+LpnKmmul2pTfUJRA760smFDj8rZ55KZ7tGTRE7DZSqr"
			+ "EC46sYKN/I5/IrEYsQJBAPge93HKGrVhArchwqKtlhj28D9X9jSWRHvLh/qaE6uS"
			+ "gUIIZYIxj7mdyjQTad2rsQMEZq4floJUTpNBNnX97YkCQQDStEcbqAMcQAtfxQag"
			+ "LHCa9MKasotrSALqkJldI816a+mu1CAWf6lsp7N1C+GR2beEJy8TqB3GKiKG3HfA"
			+ "ZNPbAkEAqqzad4qXjrbZoFF3ymFvw2Jx5pDCSnAnUaSyt96uszHG+j4+XS1seTki"
			+ "M2o7Q4Fr9PBdFV+jL6mAl2ifZtCySQJBAK7ejdFnLEWq5YWzKF+Q4lfIYSYwnkMI"
			+ "Pdtc7Nt/Fk7O9t9xD7x8cSlLOUYf+TKp5YPZXVnMAgHwAD27Bev4AlcCQQDOakib"
			+ "oMipmXLW8WJKZiBijQWUO2fiqhrcmyy2BXnUliFFPyP2DBv2INkNBUN26SHm6aW0" + "v8wLM8bLV11Z/H2O";

	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMOCGsuIgBv95ahmyttWywTN/8"
			+ "1sSI7BR8Sr5pjbrx3sd75hhuOKlkXsc4LGNY5/VTqPJiiqxHeWA1obqo3WVTo9Ju"
			+ "tbzDnklYJRKqCFWFLnKupyXEBkmB/3PVNaMVAXs6s+mOUtJW/hzvx8pCSfG0eCjn" + "/zmbpfiBXlzdxIYfMwIDAQAB";

	// 公钥用于对数据进行加密，私钥用于对数据进行解密
	// 私钥用于对数据进行签名，公钥用于对签名进行验证
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		final String data = "你123好abc你好你好aaabbb111222";

		try {
			if (false) {
				Map<String, Object> kp = RSAUtils.genKeyPair();
				String pri = RSAUtils.getPrivateKey(kp);
				String pub = RSAUtils.getPublicKey(kp);
				System.out.println("pri:" + pri);
				System.out.println("pub:" + pub);
			}

			if (true) {
				byte[] mi = RSAUtils.encryptByPublicKey(data.getBytes(), RSA_PUBLIC);
				System.out.println("mi:" + new String(mi));
				byte[] ming = RSAUtils.decryptByPrivateKey(mi, RSA_PRIVATE_PKCS8);
				System.out.println("ming:" + new String(ming));
			}

			int a = 0;
			a++;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

}
