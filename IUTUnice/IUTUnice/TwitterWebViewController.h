//
//  TwitterWebViewController.h
//  IUTUnice
//
//  Created by Wazany on 14/02/2014.
//  Copyright (c) 2014 UNS. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TwitterWebViewController : UIViewController <UIWebViewDelegate>

@property (strong,nonatomic) UIWebView* webView;
@property (strong,nonatomic) UIActivityIndicatorView *spinner;
@end

