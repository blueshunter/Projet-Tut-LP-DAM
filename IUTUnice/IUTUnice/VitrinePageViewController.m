//
//  VitrinePageViewController.m
//  IUTUnice
//
//  Created by macos on 13/02/2014.
//  Copyright (c) 2014 UNS. All rights reserved.
//

#import "VitrinePageViewController.h"

@interface VitrinePageViewController ()

@end

@implementation VitrinePageViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"Bienvenue à l'IUT";
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    _presentationImage.animationImages = [NSArray arrayWithObjects:
                                          [UIImage imageNamed:@"IUT NICE.JPG"],
                                          [UIImage imageNamed:@"IUT SOPHIA.JPG"],
                                          [UIImage imageNamed:@"IUT MENTON195.JPG"],
                                          [UIImage imageNamed:@"IUT NICE2.JPG"],
                                          [UIImage imageNamed:@"menton.jpg"],nil];
    
    _presentationImage.animationDuration = 20.00;
    _presentationImage.animationRepeatCount = 0; //infinite
    _presentationImage.contentMode = UIViewContentModeScaleAspectFit;
    [_presentationImage startAnimating];
    
    NSString *myPath = [[NSBundle mainBundle]pathForResource:@"descriptionIUT" ofType:@"txt"];
    
    NSString *presentationText = [[NSString alloc]initWithContentsOfFile:myPath encoding:NSUTF8StringEncoding error:nil];
    
    UITextView *myUITextView = [[UITextView alloc] initWithFrame:CGRectMake(0,0,_descriptionScrollView.frame.size.width,_descriptionScrollView.frame.size.height)];
    myUITextView.text = presentationText;
    myUITextView.textColor = [UIColor lightGrayColor];
    myUITextView.font = [UIFont systemFontOfSize:14];
    [myUITextView setBackgroundColor:[UIColor clearColor]];
    myUITextView.editable = NO;
    myUITextView.scrollEnabled = YES;
    [_descriptionScrollView addSubview:myUITextView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
