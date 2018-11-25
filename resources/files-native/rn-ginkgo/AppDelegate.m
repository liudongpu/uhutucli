/**
 * Copyright (c) 2015-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "AppDelegate.h"
#import <ILiveSDK/ILiveSDK.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>

#import <GinkgoIosVideo/VideoRoomView.h>
#import <GinkgoIosCore/UpdateCheck.h>



@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  
  [[NSNotificationCenter defaultCenter] addObserver: self selector: @selector(eventNotice:) name: @"MiniappNotificationEvent" object: nil];
  
  UpdateCheck *updateCheck=[UpdateCheck new];
  NSString *localBundlePath=[updateCheck upLocalBundle];
  
  NSURL *jsCodeLocation;
  
  if(![localBundlePath isEqualToString:@""]){
    jsCodeLocation = [NSURL URLWithString:localBundlePath];
    
  }else{
    jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
  }
  
  
  
  RCTRootView *rootView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
                                                      moduleName:@"[@config:appReact.workName]"
                                               initialProperties:nil
                                                   launchOptions:launchOptions];
  rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];
  
  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  UIViewController *rootViewController = [UIViewController new];
  rootViewController.view = rootView;
  
  CGRect rectStatus = [[UIApplication sharedApplication] statusBarFrame];
  CGRect textViewFrame = CGRectMake(0, rectStatus.size.height, rectStatus.size.width, 20);
  self.button = [[UIButton alloc] initWithFrame:textViewFrame];
  [self.button setTitle:@"正在通话中……" forState:UIControlStateNormal];
  self.button.titleLabel.font = [UIFont systemFontOfSize: 12.0];
  
  UIColor *white2 = [UIColor colorWithRed:252.0f/255.0f green:214.0f/255.0f blue:146.0f/255.0f alpha:1];
  [self.button setBackgroundColor:white2];
  self.button.hidden=YES;
  [self.button addTarget:self action:@selector(backBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
  
  
  [rootViewController.view addSubview:self.button];
  self.flagVideo=YES;
  //设置屏幕常亮
  [UIApplication sharedApplication].idleTimerDisabled = YES;
  
  
  
  
  
  
  [updateCheck check];
  
  
  
  
  
  self.window.rootViewController = rootViewController;
  [self.window makeKeyAndVisible];
  return YES;
}



//关闭界面退出房间
- (void)backBtnClicked:(UIButton *)sender{
  
  [self.window.rootViewController presentViewController:self.videoroom  animated:YES completion:nil];
  
  
}


-(void)eventNotice:(NSNotification*) notification{
  
  NSDictionary *dic = (NSDictionary *)[notification object];
  
  NSLog(@"eventNotice %@",dic);
  
  NSString *sEventType=[dic objectForKey:@"eventType"];
  
  dispatch_async(dispatch_get_main_queue(), ^{
    if([sEventType isEqualToString:@"nativeEventBack"]){
      self.flagVideo=NO;
      self.button.hidden=NO;
      
    }else if([sEventType isEqualToString:@"nativeEventJump"]){
      
      if(self.videoroom==nil){
        
        
        //NSString *sTargetUrl=[dic objectForKey:@"targetUrl"];
        self.videoroom = [[VideoRoomView alloc] initWithDict:dic];
        //[self.window.rootViewController.navigationController pushViewController:vc animated:YES];
        //self.window.rootViewController=vc;
        
        [self.window.rootViewController presentViewController:self.videoroom  animated:YES completion:nil];
      }
      else{
        NSLog(@"已经在视频中，不会再次创建" );
      }
      
    }else if([sEventType isEqualToString:@"nativeEventHidenav"]){
      self.videoroom=nil;
      self.flagVideo=YES;
      self.button.hidden=YES;
      
      
    }else if([sEventType isEqualToString:@"nativeEventUpgrade"]){
      UIAlertController *alertText = [UIAlertController alertControllerWithTitle:[dic objectForKey:@"showTitle"] message:[dic objectForKey:@"showContent"] preferredStyle:UIAlertControllerStyleAlert];
      //增加按钮
      [alertText addAction:[UIAlertAction actionWithTitle:@"我再想想" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
      }]];
      [alertText addAction:[UIAlertAction actionWithTitle:@"立即更新" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        NSString *str = [dic objectForKey:@"downloadUrl"]; //更换id即可
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:str]];
      }]];
      [self.window.rootViewController presentViewController:alertText animated:YES completion:nil];
      
      
    }else if([sEventType isEqualToString:@"nativeEventHttp"]){
      
      NSString *str = [dic objectForKey:@"webUrl"];
      [[UIApplication sharedApplication] openURL:[NSURL URLWithString:str]];
      
    }else if([sEventType isEqualToString:@"nativeEventToast"]){
      
      //NSString *message=[dic objectForKey:@"messageInfo"];
      
      
      
    }
  });
  
  
  
  //[self setLoginUI];
}


//在控制器销毁的时候移除通知
-(void)dealloc{
  //移除通知有两种：1.移除所有通知；2.移除指定通知
  //1.移除所有通知
  //[[NSNotificationCenter defaultCenter]removeObserver:self];
  
  //2.移除指定通知
  [[NSNotificationCenter defaultCenter] removeObserver:self name:@"MiniappNotificationEvent" object:nil];
  
}


@end
